package com.example.projectole.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projectole.R
import com.example.projectole.databinding.FragmentPesanSekarangBinding
import com.example.projectole.model.Transaksi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PesanSekarangFragment : Fragment() {
    lateinit var binding : FragmentPesanSekarangBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPesanSekarangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var hargaTotal = arguments?.getInt("hargaTotal")
        var namaproduk = arguments?.getString("namaproduk")
        var foto = arguments?.getString("foto")
        var jumlah = arguments?.getString("jumlah")

        binding.editTextBayar.setText(hargaTotal.toString())

        binding.radioGroupItem.setOnCheckedChangeListener { _, checkId ->
            if (checkId == R.id.radio_item_1){
                var ongkir = 10000
                var tambah = ongkir + hargaTotal!!
                binding.editTextBayar.setText(tambah.toString())
            } else if (checkId == R.id.radio_item_2){
                var ongkir = 6000
                var tambah = ongkir + hargaTotal!!
                binding.editTextBayar.setText(tambah.toString())
            }
        }

        binding.buttonSave.setOnClickListener{
            val ongkir = when (binding.radioGroupItem.checkedRadioButtonId) {
                R.id.radio_item_1 -> 10000
                R.id.radio_item_2 -> 6000
                else -> 0
            }

            hargaTotal = hargaTotal!! + ongkir

            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

            val transaksi = Transaksi(
                namabarang = namaproduk ?: "",
                jumlah = jumlah!!.toInt(),
                totalharga = hargaTotal!!.toInt(),
                foto = foto ?: ""
            )

            val databaseReference = FirebaseDatabase.getInstance().reference
                .child("transaksi")
                .child(currentUserUid ?: "")

            val newTransactionRef = databaseReference.push()
            newTransactionRef.setValue(transaksi)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val bundle = Bundle()
                        bundle.putInt("hargaTotal", hargaTotal!!.toInt())
                        findNavController().navigate(R.id.action_pesanSekarangFragment_to_pembayaranFragment, bundle)
                    } else {

                    }
                }
        }
    }
}
