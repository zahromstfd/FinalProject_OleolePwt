package com.example.projectole.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectole.adapter.MinumanAdapter
import com.example.projectole.databinding.FragmentMinumanBinding
import com.example.projectole.model.Minuman
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MinumanFragment : Fragment() {
    lateinit var binding : FragmentMinumanBinding
    private lateinit var databaseRef: DatabaseReference
    lateinit var adapter: MinumanAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMinumanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MinumanAdapter(ArrayList())
        val layoutManager = GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false)
        binding.rvMinuman.layoutManager = layoutManager
        binding.rvMinuman.adapter = adapter
        databaseRef = FirebaseDatabase.getInstance().getReference("minuman")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val minumanList = ArrayList<Minuman>()
                for (snapshot in dataSnapshot.children) {
                    val minuman = snapshot.getValue(Minuman::class.java)
                    minuman?.let { minumanList.add(it) }
                }
                adapter.setData(minumanList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }
}