package com.example.projectole.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectole.R
import com.example.projectole.adapter.HistoryAdapter
import com.example.projectole.databinding.FragmentNotifikasiBinding
import com.example.projectole.model.Transaksi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryFragment : Fragment() {
    lateinit var binding: FragmentNotifikasiBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotifikasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryAdapter(ArrayList())
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvHistory.layoutManager = layoutManager
        binding.rvHistory.adapter = adapter

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserUid != null) {
            databaseRef = FirebaseDatabase.getInstance().getReference("transaksi").child(currentUserUid)
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val transaksiList = ArrayList<Transaksi>()
                    for (snapshot in dataSnapshot.children) {
                        val transaksi = snapshot.getValue(Transaksi::class.java)
                        transaksi?.let { transaksiList.add(it) }
                    }
                    adapter.setData(transaksiList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }

        binding.profileIcon.setOnClickListener {
            findNavController().navigate(R.id.action_notifikasiFragment_to_profileFragment)
        }
    }
}
