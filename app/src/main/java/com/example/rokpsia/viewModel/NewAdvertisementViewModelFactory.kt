package com.example.rokpsia.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rokpsia.repository.PetRepository

class NewAdvertisementViewModelFactory(private val repository: PetRepository) : ViewModelProvider.Factory
{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return NewAdvertisementViewModel(
            repository
        ) as T
    }

}