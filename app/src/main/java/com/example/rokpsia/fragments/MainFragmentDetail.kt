package com.example.rokpsia.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.rokpsia.R
import com.example.rokpsia.databinding.FragmentMainDetailBinding
import com.google.android.material.transition.MaterialContainerTransform
import kotlin.math.roundToInt


class MainFragmentDetail: Fragment() {

    private lateinit var binding:FragmentMainDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainDetailBinding.inflate(inflater,container,false)
        binding.pet=arguments?.getParcelable("pet")

        val isFavourite = arguments?.getBoolean("isFavourite")
        if(isFavourite==true)
        {
            binding.dislikeButton.alpha=0.0f
            binding.likeButton.alpha=0.0f


            val layout1 = binding.dislikeButton
            val params1: ViewGroup.LayoutParams = layout1.layoutParams
            params1.width = 0
            params1.height = 0
            layout1.layoutParams = params1

            val layout2 = binding.likeButton
            val params2: ViewGroup.LayoutParams = layout2.layoutParams
            params2.width = 0
            params2.height = 0
            layout2.layoutParams = params2

        }

        Glide.with(requireContext()).load(binding.pet!!.uri).into(binding.photoInDetail)

        if(binding.pet?.pet?.description.isNullOrEmpty()){
            binding.description.text = "Brak"
        }
        else
        {
            binding.description.text = binding.pet?.pet?.description
        }

        handlePaws()
        return binding.root
    }

    private fun canBeInt(health: Float): Boolean {
        val valueInt = binding.pet!!.pet.health.roundToInt().toFloat()
        val valueFloat = health

        return valueInt==valueFloat
    }


    private fun handlePaws(){

        val pet = binding.pet!!.pet

        val behavior = pet.behavior!!
        val character = pet.character!!

        val calm = character.calm
        val fearful = character.fearful
        val smart = character.smart
        val family = character.family

        val active = behavior.active
        val barking = behavior.barking
        val goodBoy = behavior.goodBoy
        val appetite = behavior.appetite


        binding.spinnerItemText.text = "Spokojny"
        binding.RatingBar.rating = calm

        binding.spinnerItemText2.text = "Płochliwy"
        binding.RatingBar2.rating = fearful

        binding.spinnerItemText3.text = "Inteligentny"
        binding.RatingBar3.rating = smart

        binding.spinnerItemText4.text = "Rodzinny"
        binding.RatingBar4.rating = family



        binding.spinnerItemTextTwo.text = "Aktywny"
        binding.RatingBarTwo.rating = active

        binding.spinnerItemText2Two.text = "Szczekliwy"
        binding.RatingBar2Two.rating = barking

        binding.spinnerItemText3Two.text = "Posłuszny"
        binding.RatingBar3Two.rating = goodBoy

        binding.spinnerItemText4Two.text = "Łakomy"
        binding.RatingBar4Two.rating = appetite

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment
            duration = 600.toLong()
            scrimColor = Color.TRANSPARENT
        }
    }
}