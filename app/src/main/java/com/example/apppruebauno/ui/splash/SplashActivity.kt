package com.example.apppruebauno.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.apppruebauno.R
import com.example.apppruebauno.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    //aca solo ejecuta el splash y se va al login directamente luego de dos segundos
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)
    }
}