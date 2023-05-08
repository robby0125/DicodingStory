package com.robby.dicodingstory.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.robby.dicodingstory.R
import com.robby.dicodingstory.core.utils.Resource
import com.robby.dicodingstory.databinding.ActivityLoginBinding
import com.robby.dicodingstory.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel by viewModel<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> {
                val email = binding.edLoginEmail.text.toString().trim()
                val password = binding.edLoginPassword.text.toString()

                authViewModel.login(email, password).observe(this) {
                    when (it) {
                        is Resource.Success -> {
                            val homeIntent = Intent(this, HomeActivity::class.java)
                            startActivity(homeIntent)
                            finish()
                        }
                        is Resource.Loading -> {}
                        is Resource.Error -> {}
                    }
                }
            }

            R.id.btn_register -> {
                val registerIntent = Intent(this, RegisterActivity::class.java)
                startActivity(registerIntent)
            }
        }
    }
}