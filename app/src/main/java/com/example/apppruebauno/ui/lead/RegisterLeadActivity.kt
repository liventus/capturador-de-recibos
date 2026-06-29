package com.example.apppruebauno.ui.lead

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.apppruebauno.R
import com.example.apppruebauno.ui.home.HomeActivity
import com.google.android.material.button.MaterialButton

class RegisterLeadActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterLeadViewModel
    private lateinit var tvStepTitle: TextView
    private lateinit var pbSteps: ProgressBar
    private lateinit var btnBack: MaterialButton
    private lateinit var btnNext: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_lead)

        viewModel = ViewModelProvider(this)[RegisterLeadViewModel::class.java]

        tvStepTitle = findViewById(R.id.tvStepTitle)
        pbSteps = findViewById(R.id.pbSteps)
        btnBack = findViewById(R.id.btnBack)
        btnNext = findViewById(R.id.btnNext)

        viewModel.currentStep.observe(this) { step ->
            updateUI(step)
            showFragment(step)
        }

        btnNext.setOnClickListener {
            if (viewModel.currentStep.value == 7) {
                // VOLVER AL INICIO MANTENIENDO LOS DATOS
                val intent = Intent(this, HomeActivity::class.java)
                // CLEAR_TOP + SINGLE_TOP hace que se regrese a la instancia existente 
                // sin destruirla ni perder sus extras (Tienda, Rol, etc.)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            } else {
                viewModel.nextStep()
            }
        }

        btnBack.setOnClickListener {
            viewModel.previousStep()
        }
    }

    private fun updateUI(step: Int) {
        tvStepTitle.text = "Paso $step de 7"
        pbSteps.progress = step
        
        // El botón de atrás no se muestra en el paso 1 ni en el paso 7 (éxito)
        btnBack.visibility = if (step == 1 || step == 7) View.GONE else View.VISIBLE
        
        // Personalizar texto del botón siguiente
        btnNext.text = when(step) {
            6 -> "Enviar Solicitud"
            7 -> "Volver al Inicio"
            else -> "Siguiente"
        }
    }

    private fun showFragment(step: Int) {
        val fragment: Fragment = when (step) {
            1 -> Step1ClientFragment()
            2 -> Step2GuarantorFragment()
            3 -> Step3ReferencesFragment()
            4 -> Step4MotoFragment()
            5 -> Step5CalculatorFragment()
            6 -> Step6ReviewFragment()
            7 -> Step7SuccessFragment()
            else -> Fragment() 
        }

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragmentContainerLead, fragment)
            .commit()
    }
}
