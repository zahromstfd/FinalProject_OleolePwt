package com.example.projectole.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.projectole.R
import com.example.projectole.databinding.FragmentDetailMinumanBinding

class DetailMinumanFragment : Fragment() {
    lateinit var binding : FragmentDetailMinumanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailMinumanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var namaproduk = arguments?.getString("minuman")
        var stok = arguments?.getInt("stok")
        var keterangan = arguments?.getString("keterangan")
        var harga = arguments?.getInt("harga")
        var foto = arguments?.getString("foto")

        Log.d("Foto URL","URL : $foto")

        binding.productNameTextView.setText(namaproduk)
        //binding.quantityTextView.setText(stok.toString())
        binding.descriptionTextView.setText(keterangan)
        binding.priceTextView.setText(harga.toString())
        Glide.with(view).load(foto.toString()).into(binding.productImageView)

        binding.minusButton.setOnClickListener{
            var input = binding.quantityTextView.text.toString()
            val kurang = input.toInt() - 1
            binding.quantityTextView.setText(kurang.toString())
        }
        binding.plusButton.setOnClickListener{
            var input = binding.quantityTextView.text.toString()
            val tambah = input.toInt() + 1
            binding.quantityTextView.setText(tambah.toString())
        }
        binding.pesanButton.setOnClickListener{
            var input = binding.quantityTextView.text.toString()
            val hargaTotal = input.toInt() * harga!!.toInt()
            val bundle = Bundle()
            bundle.putInt("hargaTotal",hargaTotal)
            bundle.putString("namaproduk",namaproduk)
            bundle.putString("foto",foto)
            bundle.putString("jumlah",input)
            findNavController().navigate(R.id.action_detailMinumanFragment_to_pesanSekarangFragment,bundle)
        }
    }
}