package com.example.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class TambahFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tambah, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edtIndonesia = view.findViewById<EditText>(R.id.edtIndonesia)
        val edtInggris = view.findViewById<EditText>(R.id.edtInggris)
        val btnSimpan = view.findViewById<MaterialButton>(R.id.btnSimpan)

        btnSimpan.setOnClickListener {
            val indo = edtIndonesia.text.toString().trim()
            val ing = edtInggris.text.toString().trim()

            if (indo.isEmpty() || ing.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.pesan_input_kosong), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = KamusDatabase.getInstance(requireContext())
                db.kataDao().tambah(Kata(indonesia = indo, inggris = ing))
                edtIndonesia.text.clear()
                edtInggris.text.clear()
                Toast.makeText(requireContext(), getString(R.string.pesan_berhasil_simpan), Toast.LENGTH_SHORT).show()
            }
        }
    }
}