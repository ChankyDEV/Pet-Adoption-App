package com.example.rokpsia.viewModel.UserProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rokpsia.repository.PetRepository

class UserProfileViewModelFactory(private val repository: PetRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserProfileViewModel(
            repository
        ) as T
    }
}