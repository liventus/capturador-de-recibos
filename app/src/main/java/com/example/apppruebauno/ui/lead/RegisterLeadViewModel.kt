package com.example.apppruebauno.ui.lead

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class LeadData(
    // Paso 1: Cliente
    var clientDni: String = "",
    var clientName: String = "",
    var clientAddress: String = "",
    var clientDocPhotos: MutableList<String> = mutableListOf(),
    
    // Paso 2: Fiador
    var guarantorDni: String = "",
    var guarantorName: String = "",
    var guarantorDocPhotos: MutableList<String> = mutableListOf(),
    
    // Paso 3: Referencias
    var references: MutableList<Reference> = mutableListOf(),
    
    // Paso 4: Moto
    var motoModel: String = "",
    var motoColor: String = "",
    var motoYear: String = "",
    
    // Paso 5: Crédito
    var initialPayment: Double = 0.0,
    var selectedPlanMonths: Int = 12,
    var estimatedQuota: Double = 0.0
)

data class Reference(val name: String, val phone: String, val relationship: String)

class RegisterLeadViewModel : ViewModel() {
    private val _currentStep = MutableLiveData(1)
    val currentStep: LiveData<Int> get() = _currentStep

    val leadData = LeadData()

    fun nextStep() {
        _currentStep.value = (_currentStep.value ?: 1) + 1
    }

    fun previousStep() {
        if ((_currentStep.value ?: 1) > 1) {
            _currentStep.value = (_currentStep.value ?: 1) - 1
        }
    }
}
