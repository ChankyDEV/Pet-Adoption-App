package com.example.rokpsia.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.rokpsia.R
import com.example.rokpsia.databinding.CharacterSpinnerItemBinding
import com.example.rokpsia.databinding.SpinnerTextBinding
import com.example.rokpsia.models.SpinnerItem
import com.google.type.Color

class SpinnerAdapter(context: Context, var list: MutableList<SpinnerItem>) :
    ArrayAdapter<SpinnerItem>(context, 0, list) {



    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding:CharacterSpinnerItemBinding= DataBindingUtil
            .inflate(LayoutInflater.from(context),R.layout.character_spinner_item, parent, false)



        binding.run {
            this.spinner = list[position]
            executePendingBindings()
        }
        binding.RatingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, value, _ ->
                list[position]= SpinnerItem(binding.spinnerItemText.text.toString(),value)
            }


        return binding.root
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view =  convertView ?: LayoutInflater.from(context).inflate(
            R.layout.character_spinner_item,
            parent,
            false
        )

        if (item != null) {
            view.findViewById<TextView>(R.id.spinnerItemText).text=item.featureName
            view.findViewById<RatingBar>(R.id.RatingBar).rating=item.rate
        }
        val bindedtext:SpinnerTextBinding = DataBindingUtil
            .inflate(LayoutInflater.from(context),R.layout.spinner_text, parent, false)

        var text:TextView = bindedtext.txt
        text.textSize = 14f
        list.forEach {
            if(it.featureName == "Calm" || it.featureName == "Fearful" || it.featureName == "Smart" || it.featureName == "Family")
            {
                text.text="Uzupełnij charakter"
            }
            else
            {
                text.text="Uzupełnij zachowanie"
            }

        }

        return text
    }


}


