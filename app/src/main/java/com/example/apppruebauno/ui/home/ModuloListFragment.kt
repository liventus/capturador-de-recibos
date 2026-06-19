package com.example.apppruebauno.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebauno.R
import com.example.apppruebauno.data.model.HomeItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ModuloListFragment : Fragment() {

    private var items: List<HomeItem> = emptyList()
    private var onModuloClickListener: ((HomeItem) -> Unit)? = null

    companion object {
        private const val ARG_ITEMS = "items_json"

        fun newInstance(items: List<HomeItem>, listener: (HomeItem) -> Unit): ModuloListFragment {
            val fragment = ModuloListFragment()
            val args = Bundle()
            val json = Gson().toJson(items)
            args.putString(ARG_ITEMS, json)
            fragment.arguments = args
            fragment.onModuloClickListener = listener
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(ARG_ITEMS)?.let {
            val type = object : TypeToken<List<HomeItem>>() {}.type
            items = Gson().fromJson(it, type)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_modulo_list, container, false)
        val rvModulos = view.findViewById<RecyclerView>(R.id.rvModulos)
        rvModulos.layoutManager = GridLayoutManager(requireContext(), 2)
        rvModulos.adapter = ModuloAdapter(items) { modulo ->
            onModuloClickListener?.invoke(modulo)
        }
        return view
    }
}
