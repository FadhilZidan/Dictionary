package com.example.dictionary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class KataAdapter : ListAdapter<Kata, KataAdapter.KataViewHolder>(DiffCallback) {

    class KataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtIndonesia: TextView = itemView.findViewById(R.id.txtIndonesia)
        private val txtInggris: TextView = itemView.findViewById(R.id.txtInggris)

        fun bind(kata: Kata) {
            txtIndonesia.text = kata.indonesia
            txtInggris.text = kata.inggris
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kata, parent, false)
        return KataViewHolder(view)
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