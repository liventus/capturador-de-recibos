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
        return inflater.inflate(R.layout.fragment_modulo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.cardScannerVin)?.setOnClickListener {
            val intent = android.content.Intent(requireContext(), com.example.apppruebauno.ui.scanner.ScannerVinActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<View>(R.id.cardScannerFactura)?.setOnClickListener {
            val intent = android.content.Intent(requireContext(), com.example.apppruebauno.ui.scanner.ScannerFacturaActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<View>(R.id.cardInventario)?.setOnClickListener {
            val intent = android.content.Intent(requireContext(), com.example.apppruebauno.ui.inventory.InventoryActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<View>(R.id.cardHistorial)?.setOnClickListener {
            val intent = android.content.Intent(requireContext(), com.example.apppruebauno.ui.discrepancy.DiscrepancyActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<View>(R.id.btnRecibirMotos)?.setOnClickListener {
            // El botón naranja también puede llevar a la recepción
            val intent = android.content.Intent(requireContext(), com.example.apppruebauno.ui.scanner.ScannerVinActivity::class.java)
            startActivity(intent)
        }
    }
}
