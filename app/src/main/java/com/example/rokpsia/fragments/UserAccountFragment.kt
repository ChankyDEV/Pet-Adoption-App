package com.example.rokpsia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rokpsia.databinding.FragmentUserAccountBinding

class UserAccountFragment : Fragment() {

    private lateinit var binding: FragmentUserAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAccountBinding.inflate(inflater,container,false)

        return binding.root
    }


}