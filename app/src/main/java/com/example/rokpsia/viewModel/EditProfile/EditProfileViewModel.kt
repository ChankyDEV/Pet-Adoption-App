package com.example.rokpsia.viewModel.EditProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rokpsia.models.User
import com.example.rokpsia.repository.PetRepository
import kotlinx.coroutines.launch

class EditProfileViewModel(private val repository: PetRepository): ViewModel() {

    fun editUserData(map: Map<String,String?>){
        repository.editUserData(map)
    }

    fun getUserData(){
        viewModelScope.launch {
            repository.getUserData()
        }
    }

    fun observeDataChange(): LiveData<User?> {
        return repository.currentUserData
    }
}