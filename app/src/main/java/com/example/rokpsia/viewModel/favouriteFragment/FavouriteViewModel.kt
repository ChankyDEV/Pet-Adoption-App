package com.example.rokpsia.viewModel.favouriteFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rokpsia.models.Pet
import com.example.rokpsia.models.PetFavourite
import com.example.rokpsia.models.PetTinder
import com.example.rokpsia.models.TempPet
import com.example.rokpsia.repository.PetRepository
import kotlinx.coroutines.launch

class FavouriteViewModel(private val repository: PetRepository):ViewModel() {

    var FavouritePetList=MutableLiveData<MutableList<PetFavourite>>()

    var map= mutableMapOf<PetFavourite,Pet>()

    private val _petsPhoto = MutableLiveData<PetTinder>()
    val petsPhoto: LiveData<PetTinder>
        get()=_petsPhoto

    fun FetchFavouritesForCurrentUser(){
        viewModelScope.launch {
            repository.FetchFavouritesForCurrentUser().let {
                Log.d("1", "Udało się ${it}")
                FavouritePetList.value = it

            }
        }
    }

    fun returnPets():LiveData<MutableList<PetFavourite>>{
        return FavouritePetList
    }

    fun FindPetByID(id: String): LiveData<Pet> {
        var pet = MutableLiveData<Pet>()
        viewModelScope.launch {
            repository.FindPetByID(id).let {

               pet.value=it.value
            }
        }

        return pet
    }

    fun removeFavouriteItem(deletedItem: Pet){

        map.forEach { t, u ->
            if(deletedItem.name==u.name){
                var deletedID=t.id
                repository.removeFavouriteItem(deletedID)
            }

        }

    }
    fun getPhoto(id:Pet){
        viewModelScope.launch {
            repository.getPetPhotoByID(id)?.let {
                _petsPhoto.value = it
            }
        }
    }


}