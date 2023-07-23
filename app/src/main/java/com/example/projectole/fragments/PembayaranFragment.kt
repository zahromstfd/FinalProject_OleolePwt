package com.example.projectole.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projectole.R
import com.example.projectole.databinding.FragmentPembayaranBinding

class PembayaranFragment : Fragment() {
    lateinit var binding : FragmentPembayaranBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPembayaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var hargaTotal = arguments?.getInt("hargaTotal")


        binding.totalPaymentTextView.setText("Total Harga : $hargaTotal")

        binding.riwayatButton.setOnClickListener{
            findNavController().navigate(R.id.action_pembayaranFragment_to_notifikasiFragment)
        }
        binding.berandaButton.setOnClickListener{
            findNavController().navigate(R.id.action_pembayaranFragment_to_homeFragment)
        }
    }

}