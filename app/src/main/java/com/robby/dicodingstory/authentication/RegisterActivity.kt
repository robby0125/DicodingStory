package com.robby.dicodingstory.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robby.dicodingstory.core.utils.Resource
import com.robby.dicodingstory.databinding.ActivityRegisterBinding
import com.robby.dicodingstory.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel by viewModel<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString()

            authViewModel.register(name, email, password).observe(this) {
                when (it) {
                    is Resource.Success -> login(email, password)
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        authViewModel.login(email, password).observe(this) {
            when (it) {
                is Resource.Success -> {
                    val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }
    }
}