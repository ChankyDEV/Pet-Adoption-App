package com.example.rokpsia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.rokpsia.R
import com.example.rokpsia.databinding.FragmentRegisterBinding
import com.example.rokpsia.databinding.FragmentUserProfileBinding
import com.example.rokpsia.models.User
import com.example.rokpsia.utils.Helper
import com.example.rokpsia.viewModel.UserProfile.UserProfileViewModel
import com.example.rokpsia.viewModel.UserProfile.UserProfileViewModelFactory
import com.example.rokpsia.viewModel.registerAndLogin.RegisterViewModel
import com.example.rokpsia.viewModel.registerAndLogin.RegisterViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_user_profile.*

class UserProfileFragment : Fragment(), View.OnClickListener {

    private val viewModel: UserProfileViewModel by viewModels { UserProfileViewModelFactory(Helper.provideRepository(requireContext())) }
    private lateinit var binding: FragmentUserProfileBinding
    lateinit var navController: NavController;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        binding.editProfileBtn.setOnClickListener(this)

        displayUserData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun displayUserData(){

        viewModel.getUserData()
        viewModel.observeDataChange().observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.userNameDataTxt.text = it.username
                binding.tvUserEmailData.text = it.email

                //Wiek
                if (it.age == null || it.age == "null") {
                    binding.ageDataTxt.text = "-"

                } else {
                    binding.ageDataTxt.text = it.age
                }

                //Płeć
                if (it.sex == null) {
                    binding.sexDataTxt.text = "-"

                } else if (it.sex == "man") {
                    binding.sexDataTxt.text = "Mężczyzna"

                }else if(it.sex == "woman"){
                    binding.sexDataTxt.text = "Kobieta"
                }


                //Lokalizacja
                if (it.location != null) {

                    val location = it.location.toString()
                    val locationData = location.split(",").toTypedArray()

                    val street = locationData[0]
                    val city = locationData[1]
                    val country = locationData[2]

                    binding.tvStreetData.text = street
                    binding.tvCityData.text = city
                    binding.tvCountryData.text = country.toString()

                } else {

                    binding.tvStreetData.text = "-"
                    binding.tvCityData.text = "-"
                    binding.tvCountryData.text = "-"
                }

            }
        })

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.editProfileBtn -> {
                navController.navigate(R.id.action_userProfileFragment_to_editProfileFragment)
            }
        }

    }


}