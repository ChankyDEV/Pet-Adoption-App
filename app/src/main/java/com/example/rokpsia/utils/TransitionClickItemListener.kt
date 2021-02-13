package com.example.rokpsia.utils

import com.example.rokpsia.models.PetTinder

interface TransitionClickItemListener<I> {
    fun onClickListener(item: I, pet:PetTinder)
}