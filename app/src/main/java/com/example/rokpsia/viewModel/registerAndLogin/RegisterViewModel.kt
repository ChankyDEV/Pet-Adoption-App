package com.example.rokpsia.viewModel.registerAndLogin

import androidx.lifecycle.*
import com.example.rokpsia.repository.PetRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: PetRepository): ViewModel() {

    fun register(email: String, password: String, username: String,phoneNumber:String){
        viewModelScope.launch {
           repository.register(email, password, username,phoneNumber)
        }
    }
    fun updateSex(sex:String){
        viewModelScope.launch {
            repository.updateSex(sex)
        }
    }

    fun observeRegisterChanges(): LiveData<Boolean?> {
        return repository.okRegister
    }

}