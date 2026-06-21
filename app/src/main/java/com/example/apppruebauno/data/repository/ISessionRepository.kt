package com.example.apppruebauno.data.repository

import com.google.firebase.auth.FirebaseAuth

class ISessionRepository(private val firebaseAuth: FirebaseAuth) {

    // 1. Definimos el "QUÉ" debe hacer (Interfaz)
    interface ISessionRepository {
        fun existeSesionActiva(): Boolean
        fun cerrarSesion()
    }

    // 2. Definimos el "CÓMO" lo hace con Firebase (Implementación)
    class SessionRepositoryImpl(private val firebaseAuth: FirebaseAuth) : ISessionRepository {
        override fun existeSesionActiva(): Boolean = firebaseAuth.currentUser != null
        override fun cerrarSesion() = firebaseAuth.signOut()
    }
}