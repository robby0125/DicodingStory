package com.robby.dicodingstory.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.robby.dicodingstory.R
import com.robby.dicodingstory.authentication.LoginActivity
import com.robby.dicodingstory.home.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        mainViewModel.isLogin.observe(this) { isLogin ->
            lifecycleScope.launch {
                delay(1000)
                val intent = Intent(
                    this@MainActivity,
                    if (isLogin) HomeActivity::class.java
                    else LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
    }
}