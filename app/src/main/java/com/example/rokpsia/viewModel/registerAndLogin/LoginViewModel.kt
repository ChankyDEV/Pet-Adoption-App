package com.example.rokpsia.viewModel.registerAndLogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rokpsia.repository.PetRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: PetRepository): ViewModel() {
    fun login(email: String, password: String){
        viewModelScope.launch {
            repository.login(email, password)
        }
    }

    fun observeLoginChanges(): LiveData<Boolean?> {
        return repository.okLogin
    }
}