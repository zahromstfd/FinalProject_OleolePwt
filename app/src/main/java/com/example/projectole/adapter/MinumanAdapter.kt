package com.example.projectole.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectole.R
import com.example.projectole.databinding.ItemListBinding
import com.example.projectole.model.Minuman
import com.google.firebase.database.FirebaseDatabase

class MinumanAdapter(private val dataProduk: ArrayList<Minuman>) : RecyclerView.Adapter<MinumanAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    private val databaseRef = FirebaseDatabase.getInstance().getReference("Minuman")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataProduk.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentProduk = dataProduk[position]

        holder.binding.namaproduk.text = currentProduk.minuman
        holder.binding.harga.text = currentProduk.harga.toString()
        Glide.with(holder.itemView).load(currentProduk.foto).into(holder.binding.gambar)

        holder.binding.btnBeli1.setOnClickListener{
            var bundle = Bundle()
            bundle.putString("minuman",currentProduk.minuman)
            bundle.putInt("stok",currentProduk.stok!!.toInt())
            bundle.putString("keterangan",currentProduk.keterangan)
            bundle.putInt("harga",currentProduk.harga!!.toInt())
            bundle.putString("foto",currentProduk.foto)
            Navigation.findNavController(holder.itemView).navigate(R.id.action_minumanFragment_to_detailMinumanFragment,bundle)
        }
    }

    fun setData(newData: ArrayList<Minuman>) {
        dataProduk.clear()
        dataProduk.addAll(newData)
        notifyDataSetChanged()
    }
}