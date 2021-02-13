package com.example.rokpsia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rokpsia.R
import com.example.rokpsia.databinding.TinderItemBinding
import com.example.rokpsia.models.PetTinder
import com.example.rokpsia.utils.TransitionClickItemListener
import com.example.rokpsia.viewModel.mainFragment.MainViewModel

class TinderAdapter(private val petList:List<PetTinder>,private val viewModel:MainViewModel,private val listener: TransitionClickItemListener<View>): RecyclerView.Adapter<TinderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TinderViewHolder {
        return TinderViewHolder(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.tinder_item, parent, false),viewModel,listener)
    }

    override fun onBindViewHolder(holder: TinderViewHolder, position: Int) {
      holder.bind(petList[position])
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    fun getItems(): List<PetTinder> {
        return petList
    }
}


class TinderViewHolder(private val binding:TinderItemBinding,private val viewModel:MainViewModel,private val listener: TransitionClickItemListener<View> ):RecyclerView.ViewHolder(binding.root){

    fun bind(pet:PetTinder){
        binding.apply {

            Glide.with(this.root.context).load(pet.uri).into(binding.image)

            this.pet = pet
            executePendingBindings()
        }
    }

    init
    {
        binding.setClickListener{
            listener.onClickListener(binding.cardOne,binding.pet!!)
        }
        binding.moreButton.setOnClickListener {
            listener.onClickListener(binding.cardOne,binding.pet!!)
        }
    }

}