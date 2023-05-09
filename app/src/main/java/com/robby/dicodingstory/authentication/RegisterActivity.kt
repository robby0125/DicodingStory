package com.robby.dicodingstory.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.robby.dicodingstory.R
import com.robby.dicodingstory.core.utils.Resource
import com.robby.dicodingstory.databinding.ActivityRegisterBinding
import com.robby.dicodingstory.fragment.LoadingDialogFragment
import com.robby.dicodingstory.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var loadingDialog: LoadingDialogFragment

    private val authViewModel by viewModel<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.register)

        loadingDialog = LoadingDialogFragment()

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString()

            authViewModel.register(name, email, password).observe(this) {
                when (it) {
                    is Resource.Success -> login(email, password)

                    is Resource.Loading -> {
                        val bundle = Bundle()
                        bundle.putString(
                            LoadingDialogFragment.EXTRA_LOADING_MESSAGE,
                            getString(R.string.register_process)
                        )

                        loadingDialog.arguments = bundle
                        loadingDialog.show(supportFragmentManager, "Loading Dialog")
                    }

                    is Resource.Error -> {
                        loadingDialog.dismiss()
                        Toast.makeText(
                            this@RegisterActivity,
                            it.message ?: getString(R.string.error_default_message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        playAnimation()
    }

    private fun login(email: String, password: String) {
        authViewModel.login(email, password).observe(this) {
            when (it) {
                is Resource.Success -> {
                    val homeIntent = Intent(this@RegisterActivity, HomeActivity::class.java)
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(homeIntent)
                }

                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(250)
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(250)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(250)
        val password =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(250)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(250)

        AnimatorSet().apply {
            playSequentially(title, name, email, password, register)
            start()
        }
    }
}