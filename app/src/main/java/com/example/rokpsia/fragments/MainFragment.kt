package com.example.rokpsia.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.rokpsia.R
import com.example.rokpsia.adapter.TinderAdapter
import com.example.rokpsia.databinding.FragmentMainBinding
import com.example.rokpsia.databinding.TinderItemBinding
import com.example.rokpsia.models.PetTinder
import com.example.rokpsia.utils.Helper
import com.example.rokpsia.utils.TransitionClickItemListener
import com.example.rokpsia.viewModel.mainFragment.MainViewModel
import com.example.rokpsia.viewModel.mainFragment.MainViewModelFactory
import com.google.android.material.transition.MaterialElevationScale
import com.yuyakaido.android.cardstackview.*


class MainFragment : Fragment() {


    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: TinderAdapter
    private lateinit var manager:CardStackLayoutManager
    private val petList= mutableListOf<PetTinder>()
    private var firstTime:Boolean = true

    private var position:Int?=null

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(Helper.provideRepository(requireContext())) }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        adapter = TinderAdapter(petList,viewModel,object :TransitionClickItemListener<View>{
            override fun onClickListener(item: View, pet: PetTinder) {

                exitTransition = MaterialElevationScale(false).apply {
                    duration = 600L
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = 600L
                }

                val transitionName = "card_detail"
                val extras = FragmentNavigatorExtras(item to transitionName)
                val bundle = bundleOf("pet" to pet)

                position = petList.indexOf(pet)
                findNavController().navigate(R.id.action_mainFragment_to_main_detail,bundle,null,extras)

            }
        })

        if(firstTime) {
            viewModel.getPets()
            viewModel.returnPets().observe(viewLifecycleOwner, Observer {
                it.forEach { pet->
                    viewModel.getPhoto(pet)
                }
            })

            viewModel.petsPhoto.observe(viewLifecycleOwner, Observer {
                petList.add(it)
                adapter.notifyDataSetChanged()
            })
        }

        position?.let {prevPosition->
            binding.recycler.post {
                binding.recycler.layoutManager!!.scrollToPosition(prevPosition)
            }
        }

        var freshPet:PetTinder?=null
        var actualID:String="";
        manager = CardStackLayoutManager(requireContext(),object:CardStackListener{
            override fun onCardDisappeared(view: View?, position: Int) {
                val view = (binding.recycler.layoutManager as CardStackLayoutManager).findViewByPosition(position)

                val binding = DataBindingUtil.getBinding<TinderItemBinding>(view!!)
                val pet = binding?.pet!!
                freshPet = pet
                actualID=freshPet!!.pet.id
            }
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }
            override fun onCardSwiped(direction: Direction?) {

                when(direction){
                    Direction.Left -> {

                        viewModel.addToRejected(actualID)
                    }
                    Direction.Right ->{
                        viewModel.addToFavourite(actualID)
                    }
                }
            }
            override fun onCardCanceled() {
            }
            override fun onCardAppeared(view: View?, position: Int) {
            }
            override fun onCardRewound() {
            }
        })

        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = manager
        binding.recycler.itemAnimator = DefaultItemAnimator()


        manager.setDirections(Direction.HORIZONTAL)
        manager.setStackFrom(StackFrom.TopAndLeft)
        manager.setVisibleCount(3)

        binding.likeButton.setOnClickListener {view ->
            SetSettings(Direction.Right)
            binding.recycler.swipe()
        }

        binding.dislikeButton.setOnClickListener {view ->
            SetSettings(Direction.Left)
            binding.recycler.swipe()
        }


        binding.userButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_userProfileFragment)
        }

        return binding.root
    }

    private fun SetSettings(direction: Direction) {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager.setSwipeAnimationSetting(setting)
    }


    override fun onResume() {
        super.onResume()
     firstTime = false
    }



}