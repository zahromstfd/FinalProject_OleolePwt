package com.example.projectole.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.projectole.R
import com.example.projectole.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
            binding = FragmentHomeBinding.inflate(inflater, container, false)
            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)


        binding.btnMakanan.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_produkFragment)
        }

        binding.btnMinuman.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_minumanFragment)
        }


        binding.logout.setOnClickListener{
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            firebaseAuth.signOut()
            findNavController().navigate(R.id.action_homeFragment_to_introFragment)
        }
    }
}