package com.ketchupzz.francingsfootwear

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ketchupzz.francingsfootwear.databinding.ActivityMainBinding
import com.ketchupzz.francingsfootwear.models.Customer
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.viewmodel.AuthViewModel
import com.ketchupzz.francingsfootwear.views.SignupActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val authViewModel : AuthViewModel by viewModels<AuthViewModel>()
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignup.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
//            val customer = Customer(
//                "John",
//                "D",
//                "Doe",
//                "123 Main St",
//                "USA",
//                "12345",
//                "0977698945",
//                "09776989452",
//                "test@gmail.com",
//                "securepassword"
//            )
//            authViewModel.signup(
//                customer
//            ) {
//                when(it) {
//                    is UiState.FAILED -> Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
//                    is UiState.LOADING -> Toast.makeText(this,"Loading",Toast.LENGTH_SHORT).show()
//                    is UiState.SUCCESS -> {
//                        Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
        }
    }
}