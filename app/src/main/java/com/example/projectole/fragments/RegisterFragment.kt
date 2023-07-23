package com.example.projectole.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.projectole.R
import com.example.projectole.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class RegisterFragment : Fragment() {
    lateinit var binding : FragmentRegisterBinding
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnmasuk.setOnClickListener{
            val email = binding.EditEmailRegister.text.toString()
            val password = binding.EditPasswordRegister.text.toString()
            val konfirmasiPassword = binding.EditPasswordConfRegister.text.toString()
            val fullname = binding.EditFullnameRegister.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && konfirmasiPassword.isNotEmpty() && fullname.isNotEmpty()) {
                if (password == konfirmasiPassword) {
                    registerUser(fullname, email, password)
                } else {
                    showToast("Password dan konfirmasi password tidak sama")
                }
            } else {
                showToast("Mohon isi semua data")
            }
        }
    }


    private fun registerUser(fullname: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = fullname
                    }
                    user?.updateProfile(userUpdateProfile)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                showToast("Pendaftaran berhasil")
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                            }
                        }
                } else {
                    showToast("Pendaftaran gagal: ${task.exception?.message}")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}