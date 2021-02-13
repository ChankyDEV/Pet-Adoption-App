package com.example.rokpsia.viewModel.mainFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rokpsia.repository.PetRepository

class MainDetailsViewModelFactory(private val repository: PetRepository): ViewModelProvider.Factory
{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return MainDetailsViewModel(
            repository
        ) as T
    }

}