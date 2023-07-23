package com.example.projectole.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectole.R
import com.example.projectole.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {
    lateinit var binding : FragmentIntroBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        checkUserLoginStatus()

        binding.buttondaftar.setOnClickListener{
            findNavController().navigate(R.id.action_introFragment_to_registerFragment)
        }

        binding.buttonmasuk.setOnClickListener{
            findNavController().navigate(R.id.action_introFragment_to_loginFragment)
        }
        Log.d("User", "${sharedPreferences.getString("email", "")}")
    }

    private fun checkUserLoginStatus() {
        val email = sharedPreferences.getString("email", null)
        if (email != null) {
            findNavController().navigate(R.id.action_introFragment_to_homeFragment)
        }
    }
}
