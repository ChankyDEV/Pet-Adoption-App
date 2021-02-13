package com.example.rokpsia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rokpsia.databinding.StartSplashBinding


class StartFragment: Fragment() {

    private lateinit var binding:StartSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StartSplashBinding.inflate(inflater,container,false)


        return binding.root
    }
}