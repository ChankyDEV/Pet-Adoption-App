package com.example.rokpsia.fragments.loginAndRegister

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.rokpsia.R
import com.example.rokpsia.databinding.FragmentRegisterBinding
import com.example.rokpsia.databinding.RegisterOneBinding
import com.example.rokpsia.databinding.RegisterThreeBinding
import com.example.rokpsia.utils.Helper
import com.example.rokpsia.viewModel.registerAndLogin.RegisterViewModel
import com.example.rokpsia.viewModel.registerAndLogin.RegisterViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.register_one.view.*
import kotlinx.android.synthetic.main.register_three.view.*


class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels { RegisterViewModelFactory(Helper.provideRepository(requireContext())) }
    private lateinit var binding: FragmentRegisterBinding
    lateinit var navController: NavController;
    private lateinit var auth: FirebaseAuth

    private lateinit var firstPageView:View;
    private lateinit var secondPageView:View;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        registerObserve()

        binding.RegisterBtn.setOnClickListener {
            performAction()
        }

        binding.haveAccountText.setOnClickListener {
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        handleCarousel(binding)


        viewModel.updateSex("women")

        secondPageView.toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->


            if(isChecked) {
                var sex = "woman"
                when (checkedId) {
                    2131361913 -> {
                        sex = "woman"
                    }
                    2131361915 -> {
                        sex = "man"
                    }
                }
                viewModel.updateSex(sex)
            }
        }



        return binding.root
    }


    private fun registerObserve(){
        viewModel.observeRegisterChanges().observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    true-> {
                        navController.navigate(R.id.action_registerFragment_to_registerP2)
                    }
                    false -> {
                        Toast.makeText(context, "Rejestracja zakończona niepowodzeniem", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        navController = Navigation.findNavController(view)
    }








    private fun handleCarousel(binding: FragmentRegisterBinding){

        val listOfViews = getViewsToCarousel()
        firstPageView = listOfViews[0]
        secondPageView = listOfViews[1]
        binding.carouselView.pageCount = 2

        binding.carouselView.pageTransformer

        binding.carouselView.setViewListener {index->

            val viewChosen = listOfViews[index]

            if(index==1){
                binding.step.text = "Etap 1 z 2"
                binding.RegisterBtn.alpha = 1.0f
                binding.RegisterBtn.isEnabled = true
                val view = returnFirstView(viewChosen)
            }
            else{
                binding.step.text = "Etap 2 z 2"
                binding.RegisterBtn.alpha = 0.0f
                binding.RegisterBtn.isEnabled = false
               val view =  returnSecondView(viewChosen)

            }
             viewChosen
        }
    }

    private fun getViewsToCarousel():List<View>{
        val listOfViews = mutableListOf<View>()
        listOfViews.add(layoutInflater.inflate(R.layout.register_one, null))

        listOfViews.add(layoutInflater.inflate(R.layout.register_three, null))

        var view = listOfViews[0];
        return listOfViews
    }

    private fun returnFirstView(viewInside:View): RegisterOneBinding? {

        return DataBindingUtil.getBinding(viewInside)
    }

    private fun returnSecondView(viewInside:View): RegisterThreeBinding? {


        return DataBindingUtil.getBinding(viewInside)

    }


    private fun checkRegisterData() : Boolean {


            if(secondPageView.userNameText.text.isEmpty()){
                secondPageView.userNameText.error = "Nie podano nazwy użytkownika"
                secondPageView.userNameText.requestFocus()
                return false

            }else if(firstPageView.emailTxtView.text.isEmpty()) {
                firstPageView.emailTxtView.error = "Nie podano adresu e-mail"
                firstPageView.emailTxtView.requestFocus()
                return false

            }else if(!Patterns.EMAIL_ADDRESS.matcher(firstPageView.emailTxtView.text.toString()).matches()){
                firstPageView.emailTxtView.error = "Nieprawidłowy adres e-mail"
                firstPageView.emailTxtView.requestFocus()
                return false

            }else if (firstPageView.passwordTxtView.text.isEmpty()){
                firstPageView.passwordTxtView.error = "Nie podano hasła"
                firstPageView.passwordTxtView.requestFocus()
                return false

            }else if(firstPageView.passwordTxtView.text.length <= 6) {
                firstPageView.passwordTxtView.error = "Hasło musi zawierać 6 lub więcej znaków"
                firstPageView.passwordTxtView.requestFocus()
                return false

            }else if(secondPageView.phoneNumber.text.length!=9){
                return false
            }
            else if (firstPageView.passwordTxtView.text.toString() !=firstPageView. passwordTxtView2.text.toString()){
                firstPageView.passwordTxtView2.error = "Hasła nie są takie same"
                firstPageView.passwordTxtView2.requestFocus()
                return false
            }
            else {
                return true
            }


    }

    private fun performAction(){


        if(checkRegisterData()){
            viewModel.register(
                firstPageView.emailTxtView.text.toString(),
                firstPageView.passwordTxtView.text.toString(),
                secondPageView.userNameText.text.toString(),
                secondPageView.phoneNumber.text.toString()
            )
            navController.navigate(R.id.action_registerFragment_to_registerP2)
        }
    }

}