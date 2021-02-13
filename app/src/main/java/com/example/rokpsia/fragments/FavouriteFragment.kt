package com.example.rokpsia.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rokpsia.R
import com.example.rokpsia.adapter.FavouriteListAdapter
import com.example.rokpsia.databinding.FragmentFavouriteBinding
import com.example.rokpsia.models.Pet
import com.example.rokpsia.models.PetFavourite
import com.example.rokpsia.models.PetTinder
import com.example.rokpsia.models.TempPet
import com.example.rokpsia.utils.Helper
import com.example.rokpsia.utils.TransitionClickItemListener
import com.example.rokpsia.viewModel.favouriteFragment.FavouriteViewModel
import com.example.rokpsia.viewModel.favouriteFragment.FavouriteViewModelFactory
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.android.synthetic.main.fragment_favourite.*


class FavouriteFragment : Fragment() {

    var favouritePets= mutableListOf<PetTinder>()
    var IDS= mutableListOf<PetFavourite>()
    var loaded:Boolean=true
    lateinit var listAdapter:FavouriteListAdapter

    private val viewModel: FavouriteViewModel by viewModels { FavouriteViewModelFactory(Helper.provideRepository(requireContext())) }

    private lateinit var binding: FragmentFavouriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater,container,false)

        listAdapter = FavouriteListAdapter(favouritePets,viewModel, object :TransitionClickItemListener<Pet> {
            override fun onClickListener(item: Pet, pet: PetTinder) {

                var temp = 5;
                val bundle = bundleOf("pet" to pet,"isFavourite" to true)
                findNavController().navigate(R.id.action_favouriteFragment_to_main_detail,bundle)
            }

        })
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setLayoutManager(layoutManager)
        binding.recyclerView.setAdapter(listAdapter)

        // Get data from firestore
        getFavourites()

        if(loaded){
            viewModel.petsPhoto.observe(viewLifecycleOwner, Observer {

                favouritePets.add(it)
                listAdapter.notifyDataSetChanged()
            })
            loaded=false
        }




        return binding.root
    }

    private fun getFavourites() {
        val objectMap= mutableMapOf<PetFavourite,TempPet>()
        viewModel.FetchFavouritesForCurrentUser()
        viewModel.returnPets().observe(viewLifecycleOwner, Observer {
            IDS.addAll(it)


            IDS.forEach {id ->
                viewModel.FindPetByID(id.id).observe(viewLifecycleOwner, Observer {

                    var petTinder:PetTinder = PetTinder(Uri.EMPTY,it)
                    viewModel.getPhoto(it);
                    viewModel.map.put(id,it)
                    listAdapter.notifyDataSetChanged()
                })


            }


        })
    }


}