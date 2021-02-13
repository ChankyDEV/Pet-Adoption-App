package com.example.rokpsia.fragments.loginAndRegister

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.rokpsia.R
import com.example.rokpsia.databinding.FragmentLoginBinding
import com.example.rokpsia.utils.Helper
import com.example.rokpsia.viewModel.registerAndLogin.LoginViewModel
import com.example.rokpsia.viewModel.registerAndLogin.LoginViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_login.passwordTxtView
import java.util.Observer


class LoginFragment : Fragment(),View.OnClickListener {

    lateinit var navController: NavController;
    lateinit var navBar: BottomNavigationView
    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory(Helper.provideRepository(requireContext())) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater,container,false)

        //Ustawianie nasłuchiwania - jeśli ktoś kliknie to wywoła się metoda onClick
        binding.loginBtn.setOnClickListener(this)
        //Nasłuchiwanie przyciśnięcia napisu prowadzącego do rejestracji
        binding.registerTxtView.setOnClickListener(this)

        loginObserve()

        return binding.root

    }

    private fun loginObserve() {
        viewModel.observeLoginChanges().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                when(it){
                    true-> {
                        navController.navigate(R.id.action_login_to_mainFragment)
                    }
                    false -> {
                        Toast.makeText(context, "Logowanie nieudane", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Stworzenie kontrolera nawigacji przy stworeniu pierwszego widoku
        navController= Navigation.findNavController(view)

    }

    private fun checkLoginData():Boolean{
        if(binding.emailLoginTxtView.text.isEmpty()) {
            binding.emailLoginTxtView.error = "Nie podano nazwy użytkownika"
            binding.emailLoginTxtView.requestFocus()
            return false

        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.emailLoginTxtView.text.toString()).matches()){
            binding.emailLoginTxtView.error = "Nieprawidłowy adres e-mail"
            binding.emailLoginTxtView.requestFocus()
            return false

        }else if(passwordTxtView.text.isEmpty()) {
            binding.passwordTxtView.error = "Nie podano hasła"
            binding.passwordTxtView.requestFocus()
            return false
        }else
            return true
    }

    private fun login(){
        if (checkLoginData()) {
            viewModel.login(
                binding.emailLoginTxtView.text.toString(),
                binding.passwordTxtView.text.toString()
            )
        }
    }

    //Funkcja która realizuje button click
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.loginBtn -> {
                login()
            }

            R.id.registerTxtView -> {
                navController.navigate(R.id.action_loginFragment_to_registerFragment)
            }

        }
    }



}