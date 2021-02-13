package com.example.rokpsia.viewModel.mainFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rokpsia.models.Pet
import com.example.rokpsia.models.PetTinder
import com.example.rokpsia.repository.PetRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: PetRepository):ViewModel() {

    private val _petsInfo = MutableLiveData<MutableList<Pet>>()
    private val petsInfo: LiveData<MutableList<Pet>>
        get()=_petsInfo

    private val _petsPhoto = MutableLiveData<PetTinder>()
     val petsPhoto: LiveData<PetTinder>
        get()=_petsPhoto

    fun getPets(){
        viewModelScope.launch {
            repository.getPets().let {

                Log.d("1", "getPets: $it ")
                _petsInfo.value = it
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


    fun returnPets():LiveData<MutableList<Pet>>{
        return petsInfo
    }

    var iterator:Int=0
    fun addToFavourite(id:String) {

        viewModelScope.launch {
            repository.addToFavourite(id)
        }

    }
    fun addToRejected(id:String) {
        viewModelScope.launch {
            repository.addToRejected(id)
        }

    }

}