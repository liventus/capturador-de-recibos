package com.example.apppruebauno.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apppruebauno.data.repository.SessionRepository

class SplashViewModel : ViewModel() {

    private val repository = SessionRepository()

    private val _navegarA = MutableLiveData<String>()
    val navegarA: LiveData<String> = _navegarA

    fun validarSesion() {
        //val haySesion = repository.existeSesionActiva()

        if (false) {
            _navegarA.value = "HOME"
        } else {
            _navegarA.value = "LOGIN"
        }
    }
}