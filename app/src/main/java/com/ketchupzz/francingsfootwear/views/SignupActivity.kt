package com.ketchupzz.francingsfootwear.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
}