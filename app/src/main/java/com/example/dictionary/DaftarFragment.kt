package com.example.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class DaftarFragment : Fragment() {

    private val adapter = KataAdapter()

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
}