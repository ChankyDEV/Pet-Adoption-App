package com.example.rokpsia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rokpsia.databinding.FragmentDescriptionBinding


class DescriptionFragment : Fragment() {

    private lateinit var binding:FragmentDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDescriptionBinding.inflate(inflater,container,false)



        return binding.root
    }


}