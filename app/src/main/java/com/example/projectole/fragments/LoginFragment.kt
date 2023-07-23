package com.example.projectole.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectole.R
import com.example.projectole.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        binding.btnmasuk.setOnClickListener {
            val email = binding.EditEmailLogin.text.toString()
            val password = binding.EditPasswordLogin.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                processLogin(email, password)
            } else {
                showToast("Silahkan isi username dan password")
            }
        }
    }

    private fun processLogin(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                showToast("Login berhasil")
                val editor = sharedPreferences.edit()
                editor.putString("email", email)
                editor.apply()

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
            .addOnFailureListener { error ->
                showToast("Login gagal: ${error.localizedMessage}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
