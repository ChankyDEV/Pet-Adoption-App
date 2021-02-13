package com.example.rokpsia.viewModel.registerAndLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rokpsia.repository.PetRepository
import kotlinx.coroutines.launch

class Register2ViewModel(private val repository: PetRepository): ViewModel() {


    fun updateLocationToUser(location:String){
        viewModelScope.launch {
            repository.updateLocationToUser(location)
        }
    }

    fun updateSex(sex:String){
        viewModelScope.launch {
            repository.updateSex(sex)
        }
    }

}