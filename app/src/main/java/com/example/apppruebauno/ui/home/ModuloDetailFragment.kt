package com.example.apppruebauno.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apppruebauno.R

class ModuloDetailFragment : Fragment() {

    companion object {
        private const val ARG_MODULO_NAME = "modulo_name"

        fun newInstance(moduloName: String): ModuloDetailFragment {
            val fragment = ModuloDetailFragment()
            val args = Bundle()
            args.putString(ARG_MODULO_NAME, moduloName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Por ahora cargamos el layout estático que creamos basado en la imagen
        return inflater.inflate(R.layout.fragment_modulo_detail, container, false)
    }
}
