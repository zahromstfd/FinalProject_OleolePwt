package com.example.projectole.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectole.databinding.HistoryListBinding
import com.example.projectole.model.Transaksi

class HistoryAdapter(private val dataProduk: ArrayList<Transaksi>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(val binding: HistoryListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataProduk.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.namaproduk.text = dataProduk[position].namabarang
        holder.binding.hargaTotal.text = dataProduk[position].totalharga.toString()
        holder.binding.jumlahBarang.text = dataProduk[position].jumlah.toString()
        Glide.with(holder.itemView).load(dataProduk[position].foto).into(holder.binding.gambar)

    }

    fun setData(newData: ArrayList<Transaksi>) {
        dataProduk.clear()
        dataProduk.addAll(newData)
        notifyDataSetChanged()
    }
}