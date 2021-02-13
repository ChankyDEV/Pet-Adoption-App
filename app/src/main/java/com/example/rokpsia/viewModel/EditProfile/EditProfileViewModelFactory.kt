package com.example.rokpsia.viewModel.EditProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rokpsia.repository.PetRepository

class EditProfileViewModelFactory(private val repository: PetRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return EditProfileViewModel(
            repository
        ) as T
    }
}