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
import com.robby.dicodingstory.databinding.ActivityLoginBinding
import com.robby.dicodingstory.fragment.LoadingDialogFragment
import com.robby.dicodingstory.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingDialog: LoadingDialogFragment

    private val authViewModel by viewModel<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.login)

        loadingDialog = LoadingDialogFragment()

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)

        playAnimation()
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

                        is Resource.Loading -> {
                            val bundle = Bundle()
                            bundle.putString(
                                LoadingDialogFragment.EXTRA_LOADING_MESSAGE,
                                getString(R.string.login_process)
                            )

                            loadingDialog.arguments = bundle
                            loadingDialog.show(supportFragmentManager, "Loading Dialog")
                        }

                        is Resource.Error -> {
                            loadingDialog.dismiss()
                            Toast.makeText(
                                this@LoginActivity,
                                it.message ?: getString(R.string.error_default_message),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            R.id.btn_register -> {
                val registerIntent = Intent(this, RegisterActivity::class.java)
                startActivity(registerIntent)
            }
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvTitleLogin, View.ALPHA, 1f).setDuration(250)
        val subtitle =
            ObjectAnimator.ofFloat(binding.tvSubtitleLogin, View.ALPHA, 1f).setDuration(250)
        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(250)
        val password =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(250)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(250)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(250)

        AnimatorSet().apply {
            playSequentially(title, subtitle, email, password, login, register)
            start()
        }
    }
}