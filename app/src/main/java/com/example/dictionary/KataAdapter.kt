package com.example.dictionary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class KataAdapter(
    private val onDeleteClick: ((Kata) -> Unit)? = null
) : ListAdapter<Kata, KataAdapter.KataViewHolder>(DiffCallback) {

    class KataViewHolder(
        itemView: View,
        private val onDeleteClick: ((Kata) -> Unit)?
    ) : RecyclerView.ViewHolder(itemView) {
        private val txtIndonesia: TextView = itemView.findViewById(R.id.txtIndonesia)
        private val txtInggris: TextView = itemView.findViewById(R.id.txtInggris)
        private val btnHapus: ImageView = itemView.findViewById(R.id.btnHapus)

        fun bind(kata: Kata) {
            txtIndonesia.text = kata.indonesia
            txtInggris.text = kata.inggris
            btnHapus.setOnClickListener {
                onDeleteClick?.invoke(kata)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kata, parent, false)
        return KataViewHolder(view, onDeleteClick)
    }

    override fun onBindViewHolder(holder: KataViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Kata>() {
        override fun areItemsTheSame(oldItem: Kata, newItem: Kata): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Kata, newItem: Kata): Boolean {
            return oldItem == newItem
        }
    }
}