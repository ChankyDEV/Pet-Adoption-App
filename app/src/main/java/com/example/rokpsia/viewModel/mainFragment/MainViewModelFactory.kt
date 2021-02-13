package com.example.rokpsia.viewModel.mainFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rokpsia.repository.PetRepository

class MainViewModelFactory(private val repository: PetRepository): ViewModelProvider.Factory
{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return MainViewModel(
            repository
        ) as T
    }

}