package com.example.rokpsia.viewModel.favouriteFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rokpsia.repository.PetRepository

class FavouriteViewModelFactory(private val repository: PetRepository): ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return FavouriteViewModel(
            repository
        ) as T
    }

}