package com.example.rokpsia.viewModel.mainFragment

import com.example.rokpsia.repository.PetRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rokpsia.models.Pet
import com.example.rokpsia.models.PetTinder
import kotlinx.coroutines.launch

class MainDetailsViewModel (private val repository: PetRepository) : ViewModel() {

    private val _location = MutableLiveData<String>()
    private val location: LiveData<String>
        get()=location

    private val _phonenumber = MutableLiveData<String>()
    val phonenumber: LiveData<String>
        get()=_phonenumber

    fun downloadPhoneNumber(uid:String){
        viewModelScope.launch {
            repository.getPhoneNumber(uid).let {
                _phonenumber.value=it
            }
        }
    }

    fun getPhoneNumber():LiveData<String>{
        return phonenumber
    }

    fun getUserLocation() : String {
        viewModelScope.launch {

        }
        return ""
    }
}