package com.example.rokpsia.fragments

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.rokpsia.R

import com.example.rokpsia.utils.Helper
import com.example.rokpsia.viewModel.EditProfile.EditProfileViewModel
import com.example.rokpsia.viewModel.EditProfile.EditProfileViewModelFactory
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_main_detail.*


class EditProfileFragment : Fragment(), View.OnClickListener {

    private val viewModel: EditProfileViewModel by viewModels { EditProfileViewModelFactory(Helper.provideRepository(requireContext())) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnAddLocation).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnSave).setOnClickListener(this)
        view.findViewById<ImageButton>(R.id.btnBackToProfileFragment).setOnClickListener(this)

        displayUserData()

    }

    private fun setUserData(){
        var age: String? = view?.findViewById<TextView>(R.id.etAge)?.text.toString()

        var sex: String? = null
        if(rbMale.isChecked){
            sex = "man"

        }else if(rbFemale.isChecked){
            sex = "woman"
        }

        var map = mapOf<String, String?>(
            "age" to age,
            "sex" to sex,
        )

        viewModel.editUserData(map)
    }

    private fun displayUserData(){
        viewModel.getUserData()
        viewModel.observeDataChange().observe(viewLifecycleOwner, Observer {
            it.let {
                if(it?.age != null && it?.age != "null")
                {
                    etAge.append(it?.age.toString())
                }


                if(it?.sex != null && it.sex == "woman"){
                    rgSex.check(R.id.rbFemale)

                }else if(it?.sex != null && it.sex == "man"){
                    rgSex.check(R.id.rbMale)
                }

                if (it?.location == null){
                    tvLocation.text = "Brak lokalizacji"

                }else if(it?.location != null){
                    tvLocation.text = it.location.toString()
                }

            }
        })
    }

    private fun goToMap(){
        val bundle = bundleOf("isFromEditProfile" to true)
        findNavController().navigate(R.id.action_editProfileFragment_to_registerP2, bundle)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnSave -> setUserData()

            R.id.btnAddLocation -> goToMap()

            R.id.btnBackToProfileFragment -> findNavController().navigate(R.id.action_editProfileFragment_to_userProfileFragment)
        }

    }


}