package com.example.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class DaftarFragment : Fragment() {

    private lateinit var adapter: KataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daftar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val txtJumlah = view.findViewById<TextView>(R.id.txtJumlah)
        val rvDaftarKata = view.findViewById<RecyclerView>(R.id.rvDaftarKata)

        adapter = KataAdapter(onDeleteClick = { kata ->
            tampilkanKonfirmasiHapus(kata)
        })

        rvDaftarKata.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val db = KamusDatabase.getInstance(requireContext())
                db.kataDao().ambilSemua().collect { list ->
                    adapter.submitList(list)
                    txtJumlah.text = getString(R.string.jumlah_kata, list.size)
                }
            }
        }
    }

    private fun tampilkanKonfirmasiHapus(kata: Kata) {
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
                }
            }
            .setNegativeButton(getString(R.string.dialog_hapus_negatif), null)
            .show()
    }
}