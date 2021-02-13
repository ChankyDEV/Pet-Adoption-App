package com.example.rokpsia.viewModel.registerAndLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rokpsia.repository.PetRepository

class Register2ViewModelFactory(private val repository: PetRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return Register2ViewModel(repository) as T
    }

}