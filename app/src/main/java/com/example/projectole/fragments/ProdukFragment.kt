package com.example.projectole.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectole.adapter.ProdukAdapter
import com.example.projectole.databinding.FragmentProdukBinding
import com.example.projectole.model.Keranjang
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProdukFragment : Fragment() {
    lateinit var binding : FragmentProdukBinding
    private lateinit var databaseRef: DatabaseReference
    lateinit var adapter: ProdukAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProdukAdapter(ArrayList())
        val layoutManager = GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false)
        binding.rvProduk.layoutManager = layoutManager
        binding.rvProduk.adapter = adapter
        databaseRef = FirebaseDatabase.getInstance().getReference("keranjang")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val keranjangList = ArrayList<Keranjang>()
                for (snapshot in dataSnapshot.children) {
                    val keranjang = snapshot.getValue(Keranjang::class.java)
                    keranjang?.let { keranjangList.add(it) }
                }
                adapter.setData(keranjangList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }
}