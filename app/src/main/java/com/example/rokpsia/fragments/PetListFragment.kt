package com.example.rokpsia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rokpsia.databinding.FragmentPetListBinding


class PetListFragment : Fragment() {

    private lateinit var binding: FragmentPetListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPetListBinding.inflate(inflater,container,false)

        return binding.root
    }

}