package com.example.rokpsia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rokpsia.R
import com.example.rokpsia.databinding.FavouriteItemBinding
import com.example.rokpsia.models.Pet
import com.example.rokpsia.models.PetTinder

import com.example.rokpsia.models.TempPet
import com.example.rokpsia.utils.TransitionClickItemListener
import com.example.rokpsia.viewModel.favouriteFragment.FavouriteViewModel
import kotlinx.android.synthetic.main.favourite_item.view.*

class FavouriteListAdapter(private var favouritePetList: MutableList<PetTinder>, private var viewmodel:FavouriteViewModel,private var listener:TransitionClickItemListener<Pet>): RecyclerView.Adapter<FavouriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.favourite_item, parent, false),listener)


    }

    override fun getItemCount(): Int {
        return favouritePetList.size
    }



    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(favouritePetList[position])

        holder.itemView.delete_item_icon.setOnClickListener {
            removeItem(position)
        }

    }




    private fun removeItem(position: Int) {

        viewmodel.removeFavouriteItem(favouritePetList[position].pet)
        favouritePetList.removeAt(position)
        notifyDataSetChanged()
    }


}

class FavouriteViewHolder(private val binding: FavouriteItemBinding, private var listener:TransitionClickItemListener<Pet>):RecyclerView.ViewHolder(binding.root){

    init {

            binding.itemClick.setOnClickListener { view ->
                listener.onClickListener(binding.pet!!.pet,binding.pet!!)
            }



    }

    fun bind(pet: PetTinder){

        binding.apply {

            Glide.with(this.root.context).load(pet.uri).into(binding.circleImageView)
            this.pet = pet
            executePendingBindings()
        }
    }

}