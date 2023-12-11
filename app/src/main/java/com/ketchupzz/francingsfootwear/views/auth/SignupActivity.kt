package com.ketchupzz.francingsfootwear.views.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ketchupzz.francingsfootwear.databinding.ActivitySignupBinding
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonCreateAccount.setOnClickListener {
            val name = binding.inputFullname.text.toString()
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val repeatPassword = binding.inputRepeatPassword.text.toString()
            if (name.isEmpty())  {
                binding.layoutFullname.error = "Enter fullname"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.layoutEmail.error = "enter email"
                return@setOnClickListener

            }
            if (password.isEmpty()) {
                binding.inputPassword.error = "enter password"
                return@setOnClickListener
            }
            if (password.length < 8) {
                binding.inputPassword.error = "password should have at least 8 characters"
                return@setOnClickListener
            }
            if (password != repeatPassword) {
                binding.layoutRepeatPassword.error = "password doesn't match!"
                return@setOnClickListener
            }
            signup(email, password, name)
        }
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    fun signup(email :String,password  : String,name : String) {
        authViewModel.signup(email,password,name) {
            when(it) {
                is UiState.FAILED -> {
                    binding.buttonCreateAccount.isEnabled = true
                    binding.buttonCreateAccount.text = "Creating Account"
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    binding.buttonCreateAccount.isEnabled = false
                    binding.buttonCreateAccount.text = "Creating account...."
                }
                is UiState.SUCCESS -> {
                    binding.buttonCreateAccount.isEnabled = true
                    binding.buttonCreateAccount.text = "Creating Account"
                    Toast.makeText(binding.root.context,"Successfully Logged in",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}