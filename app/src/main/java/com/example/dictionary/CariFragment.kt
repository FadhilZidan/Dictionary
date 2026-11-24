package com.example.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class CariFragment : Fragment() {

    private val adapter = KataAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cari, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edtCari = view.findViewById<EditText>(R.id.edtCari)
        val btnCari = view.findViewById<MaterialButton>(R.id.btnCari)
        val txtKosong = view.findViewById<TextView>(R.id.txtKosong)
        val rvHasilCari = view.findViewById<RecyclerView>(R.id.rvHasilCari)

        rvHasilCari.adapter = adapter

        fun laksanakanPencarian() {
            val query = edtCari.text.toString().trim()
            lifecycleScope.launch {
                val db = KamusDatabase.getInstance(requireContext())
                val hasil = db.kataDao().cari(query)
                adapter.submitList(hasil)
                if (hasil.isEmpty()) {
                    txtKosong.visibility = View.VISIBLE
                    rvHasilCari.visibility = View.GONE
                } else {
                    txtKosong.visibility = View.GONE
                    rvHasilCari.visibility = View.VISIBLE
                }
            }
        }

        btnCari.setOnClickListener {
            laksanakanPencarian()
        }

        edtCari.doOnTextChanged { _, _, _, _ ->
            laksanakanPencarian()
        }

        laksanakanPencarian()
    }
}