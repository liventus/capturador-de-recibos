package com.example.apppruebauno.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.apppruebauno.R
import com.example.apppruebauno.ui.home.HomeActivity
import com.example.apppruebauno.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        observarViewModel()

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.validarSesion()
        }, 2000)
    }

    private fun observarViewModel() {
        viewModel.navegarA.observe(this) { destino ->

            when (destino) {
                "HOME" -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }

                "LOGIN" -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}