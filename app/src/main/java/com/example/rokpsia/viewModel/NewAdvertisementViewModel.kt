package com.example.rokpsia.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rokpsia.models.Pet
import com.example.rokpsia.repository.PetRepository
import kotlinx.coroutines.launch
import com.example.rokpsia.databinding.FragmentNewAdvertisementBinding
import com.example.rokpsia.models.PetTinder

class NewAdvertisementViewModel(private val repository: PetRepository): ViewModel() {

    private val _petsPhoto = MutableLiveData<PetTinder>()
    val petsPhoto: LiveData<PetTinder>
        get()=_petsPhoto

    fun addPetToDatabase(pet:Pet,image:ByteArray){
        viewModelScope.launch {
                repository.addPet(pet,image)
            }
        }

    fun isFormValid(view: FragmentNewAdvertisementBinding):Boolean{

            if (view.nameEditText.text.isEmpty()) {
            view.nameEditText.error="Podaj imię"
            view.nameEditText.requestFocus()
            return  false
            }
            else if (view.descriptionEditText.text.isEmpty()) {
                view.descriptionEditText.error="Wpisz opis"
                view.descriptionEditText.requestFocus()
                return  false
            }
            else if (view.szczepieniaEditText.text.isEmpty()) {
                view.szczepieniaEditText.error="Uzupełnij szczepienia"
                view.szczepieniaEditText.requestFocus()
                return  false
            }

        return true
    }

    fun observeAddingSucces(): LiveData<Boolean?> {
        return repository.successfullyAdded
    }





}

