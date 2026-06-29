package com.example.apppruebauno.ui.wallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebauno.R
import com.example.apppruebauno.data.model.WalletItem

class WalletActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        findViewById<android.view.View>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val rvWallet = findViewById<RecyclerView>(R.id.rvWallet)
        rvWallet.layoutManager = LinearLayoutManager(this)
        
        val mockData = listOf(
            WalletItem("1", "Juan Pérez", "Honda CB500X", "COTIZACIÓN", "Hace 2h", "J", "#E65100"),
            WalletItem("2", "Ana García", "Yamaha MT-07", "NEGOCIACIÓN", "Ayer", "A", "#A52A2A"),
            WalletItem("3", "Luis Torres", "Honda CRF300L", "PROSPECTO", "3 días", "L", "#666666")
        )
        
        rvWallet.adapter = WalletAdapter(mockData) { item ->
            val intent = Intent(this, WalletDetailActivity::class.java).apply {
                putExtra("EXTRA_NAME", item.name)
                putExtra("EXTRA_MODEL", item.model)
                putExtra("EXTRA_STATUS", item.status)
                // En un caso real, el item tendría el DNI
            }
            startActivity(intent)
        }
    }
}
