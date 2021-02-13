package com.example.rokpsia.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rokpsia.db.FavPetsDao
import com.example.rokpsia.models.*
import com.example.rokpsia.pocztaPolskaAPI.PolishPostService
import com.example.rokpsia.utils.LocationProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PetRepository(private val locationProvider: LocationProvider, private val favPetsDao: FavPetsDao, private val service: PolishPostService) {


    private val fireStore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val users = fireStore.collection("users")
    private val pets = fireStore.collection("pets")
    private val indexes = mutableListOf<String>()

    private val _successfullyAdded = MutableLiveData<Boolean?>()
    val successfullyAdded: LiveData<Boolean?>
        get() = _successfullyAdded

    private val _currentUserData = MutableLiveData<User?>()
    val currentUserData: LiveData<User?>
        get() = _currentUserData

    private val _okLogin = MutableLiveData<Boolean?>()
    val okLogin: LiveData<Boolean?>
        get() = _okLogin

    private val _okRegister = MutableLiveData<Boolean?>()
    val okRegister: LiveData<Boolean?>
        get() = _okRegister


    suspend fun getPets(): MutableList<Pet> = suspendCoroutine { cont ->

        GlobalScope.launch {
            val idsRejected = checkIfRejectedAlready()
            val idsFavourites = checkIfFavouriteAlready()

            val together  = mutableListOf<String>()
            together.addAll(idsRejected)
            together.addAll(idsFavourites)

            val returnPets = mutableListOf<Pet>()

            Log.d("1", "UWAGA $idsRejected AND $idsFavourites ")

            val user = getUserData()

            Log.d("1", "AAAA ${_currentUserData.value} ")

            fireStore.collection("pets").get().addOnSuccessListener {
                indexes.clear()
                it.documents.forEach { doc -> indexes.add(doc.id) }
                val petIDs = it.toObjects(Pet::class.java)


                petIDs.forEach { pet->
                    if(!together.contains(pet.id)){
                        Log.d("1", "SIEMANO ZWRACAM $pet ")

                        var voivodeship= user.voivodeship
                        if(voivodeship==null){
                            voivodeship=""
                        }

                        if(pet.voivodeship==voivodeship)
                        {
                            returnPets.add(pet)
                        }
                    }
                }
                cont.resume(returnPets)
            }.addOnFailureListener { Log.d("1", "getPets: FAILURE $it") }
        }
    }


    suspend fun getPetPhotoByID(pet:Pet):PetTinder? = suspendCoroutine {cont->


        FirebaseStorage
            .getInstance()
            .getReferenceFromUrl("gs://rokpsia.appspot.com/images/${pet.id}/1")
            .downloadUrl
            .addOnFailureListener {
                cont.resume(null)

            }.addOnSuccessListener {
                cont.resume(PetTinder(it,pet))
            }
    }

    private suspend fun checkIfRejectedAlready() = suspendCoroutine<MutableList<String>>{ cont->
        val id = auth.uid ?: return@suspendCoroutine

        val listIds = mutableListOf<String>()

        fireStore.collection("users").document(id).collection("Rejected").get()
            .addOnSuccessListener {
            it.documents.forEach {doc-> listIds.add(doc.id) }
                cont.resume(listIds)
        }
    }


    private suspend fun checkIfFavouriteAlready() = suspendCoroutine<MutableList<String>>{cont->
        val id = auth.uid ?: return@suspendCoroutine

        val listIds = mutableListOf<String>()
        fireStore.collection("users").document(id).collection("favourites").get()
            .addOnSuccessListener {
                it.documents.forEach {doc-> listIds.add(doc.id) }
                cont.resume(listIds) }
    }

    suspend fun updateLocationToUser(location:String){
        val id = auth.uid ?: return
        val locationSplit = location.split(',')
        val city = locationSplit[1].split(' ').last()
        val country = location.split(", ").last()
        val postalCode =  locationSplit[1].split(' ')[1]

        coroutineScope {
            launch {
                val result = service.getPolishPostService(postalCode)
                users.document(id).update("location" ,location,"voivodeship",result[0].voivodeship,"country",country).await()
            }
        }
    }


    suspend fun updateSex(sex:String){
        val id = auth.uid ?: return
        users.document(id).update("sex",sex).await()
    }

    suspend fun addUser(user: User) {
        val id = auth.uid ?: return
        user.uid = id
        users.add(user).await()
    }

      suspend fun addPet(pet: Pet,image:ByteArray) {

         val id = auth.uid ?: return
         pet.userID = id
         val petId = "$id-${pet.name}-${pet.age}"
         pet.id = petId
         addPetPhoto(pet,image)

        // get location from user

          val user = getUserData()
          Log.d("1", "addPet $user ")
         pet.voivodeship = user.voivodeship!!

         pets.document(petId).set(pet).addOnSuccessListener {

            _successfullyAdded.value = true

             // add your own pet to rejected
             fireStore.collection("users")
                 .document(auth.currentUser?.uid.toString())
                 .collection("Rejected")
                 .document(petId)
                 .set(PetFavourite(petId))

        }.addOnFailureListener {
             _successfullyAdded.value = false
        }
    }

    private  fun getUserLocation():String? {
        return _currentUserData.value?.voivodeship;
    }

    private fun addPetPhoto(pet:Pet,image:ByteArray){

        val id = pet.id
        Log.d("1", "ABCD $id ")

       firebaseStorage.getReference("/images/$id/1").putBytes(image).addOnSuccessListener {
           Log.d("1", "sztos ")
       }.addOnFailureListener { Log.d("1", "nie sztos ") }
    }

    fun register(email: String, password: String, username: String,phoneNumber:String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = auth.currentUser
                _okRegister.value = true
                addUserData(username, email,phoneNumber)

            }.addOnFailureListener {
                _okRegister.value = false

            }
    }

    fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = auth.currentUser
                _okLogin.value = true

            }.addOnFailureListener {
                _okLogin.value = false
            }
    }

    suspend fun getUserData():User {

        val userSnapshot =
            fireStore
                .collection("users")
                .document(auth.currentUser?.uid.toString())
                .get()
                .await()

        val user = userSnapshot.toObject(User::class.java)!!
        _currentUserData.postValue(user)

        Log.d("1", "ABCD $userSnapshot $user ${  _currentUserData.value} ")

        return user
    }

    fun startLocationUpdates() = locationProvider.getStartLocation() //i to dalej trzeba impl

    fun stopLocationUpdates() = locationProvider.getStopLocation()

    private fun addUserData(username: String, email: String,phoneNumber: String) {
        var newUser = User(auth.currentUser?.uid.toString(), username, "", email, "",
            "", null, null, null, null, null,phoneNumber
        )

        fireStore.collection("users")
            .document(auth.currentUser?.uid.toString())
            .set(newUser)
            .addOnSuccessListener {
                Log.d("Powodzenie", "Udało się dodać użytkownika")
            }.addOnFailureListener {
                Log.d("Niepowodzenie", "Nie udało się dodać użytkownika")
            }
    }

    fun editUserData(map: Map<String, String?>) {
        fireStore
            .collection("users")
            .document(auth.currentUser?.uid.toString())
            .update(map)
            .addOnSuccessListener {
                Log.d("UDALO SIE", "DANE ZAKTUALIZOWANE")
            }
            .addOnFailureListener {
                Log.d("NIE UDALO SIE", "DANE NIE ZOSTALY ZAKTUALIZOWANE")
            }

    }



    suspend fun FetchFavouritesForCurrentUser(): MutableList<PetFavourite> =
        suspendCoroutine { cont ->
            fireStore.collection("users").document(auth.currentUser?.uid.toString())
                .collection("favourites").get()
                .addOnSuccessListener {
                    cont.resume(it.toObjects(PetFavourite::class.java))
                }.addOnFailureListener { Log.d("1", "getPets: FAILURE $it") }
        }

    suspend fun getPhoneNumber(uid:String) :  String?{
        return suspendCoroutine { cont ->
            fireStore.collection("users").document(uid).get()
                .addOnSuccessListener {
                    cont.resume(it.toObject(User::class.java)?.phoneNumber)
                }.addOnFailureListener {

                }
        }
    }


    fun addToFavourite(id:String) {

            fireStore.collection("users")
                .document(auth.currentUser?.uid.toString())
                .collection("favourites")
                .document(id)
                .set(PetFavourite(id))

    }

    suspend fun FindPetByID(id: String):LiveData<Pet> = suspendCoroutine { cont ->
         var pet=MutableLiveData<Pet>()
         fireStore.collection("pets").document(id).get().addOnSuccessListener {
             pet.value = it.toObject(Pet::class.java)!!
             cont.resume(pet)
         }
    }

    fun removeFavouriteItem(deletedID: String) {
        fireStore.collection("users")
            .document(auth.currentUser?.uid.toString())
            .collection("favourites")
            .document(deletedID).delete()

        fireStore.collection("users")
            .document(auth.currentUser?.uid.toString())
            .collection("Rejected")
            .document(deletedID)
            .set(PetFavourite(deletedID))

    }

    fun addToRejected(id: String) {

            fireStore.collection("users")
                .document(auth.currentUser?.uid.toString())
                .collection("Rejected")
                .document(id)
                .set(PetFavourite(id))

    }


}