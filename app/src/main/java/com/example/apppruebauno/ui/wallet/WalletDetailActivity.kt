package com.example.apppruebauno.ui.wallet

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.apppruebauno.R

class WalletDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_detail)

        val name = intent.getStringExtra("EXTRA_NAME") ?: "N/A"
        val model = intent.getStringExtra("EXTRA_MODEL") ?: "N/A"
        val status = intent.getStringExtra("EXTRA_STATUS") ?: "N/A"
        val dni = intent.getStringExtra("EXTRA_DNI") ?: "72839401" // Mock DNI

        findViewById<TextView>(R.id.tvDetailName).text = name
        findViewById<TextView>(R.id.tvDetailMoto).text = model
        findViewById<TextView>(R.id.tvDetailStatus).text = status
        findViewById<TextView>(R.id.tvDetailDni).text = dni

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
