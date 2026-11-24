package com.example.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class CariFragment : Fragment() {

    private lateinit var adapter: KataAdapter

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

        adapter = KataAdapter(onDeleteClick = { kata ->
            tampilkanKonfirmasiHapus(kata, edtCari.text.toString().trim())
        })

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

    private fun tampilkanKonfirmasiHapus(kata: Kata, query: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_hapus_judul))
            .setMessage(getString(R.string.dialog_hapus_pesan, kata.indonesia))
            .setPositiveButton(getString(R.string.dialog_hapus_positif)) { _, _ ->
                lifecycleScope.launch {
                    val db = KamusDatabase.getInstance(requireContext())
                    db.kataDao().hapus(kata)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.pesan_berhasil_hapus, kata.indonesia),
                        Toast.LENGTH_SHORT
                    ).show()
                    val hasilBaru = db.kataDao().cari(query)
                    adapter.submitList(hasilBaru)
                    if (hasilBaru.isEmpty()) {
                        view?.findViewById<TextView>(R.id.txtKosong)?.visibility = View.VISIBLE
                        view?.findViewById<RecyclerView>(R.id.rvHasilCari)?.visibility = View.GONE
                    }
                }
            }
            .setNegativeButton(getString(R.string.dialog_hapus_negatif), null)
            .show()
    }
}