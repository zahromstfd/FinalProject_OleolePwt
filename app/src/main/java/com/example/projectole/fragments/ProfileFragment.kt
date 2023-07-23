package com.example.projectole.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.projectole.R
import com.example.projectole.databinding.FragmentProfileBinding
import com.example.projectole.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private var getFile: File? = null
    private var selectedGender: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton: View = binding.radioGroup.findViewById(checkedId)
            val index: Int = binding.radioGroup.indexOfChild(radioButton)

            selectedGender = when (index) {
                0 -> "Laki-laki"
                1 -> "Perempuan"
                else -> ""
            }
        }

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val uIdRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
        uIdRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val snapshot = it.result
                val name = snapshot.child("email").getValue<String>()
                val image = snapshot.child("image").getValue<String>()
                val password = snapshot.child("pass").getValue<String>()
                val nama = snapshot.child("fullname").getValue<String>()
                val tanggalLahir = snapshot.child("date").getValue<String>()
                val alamat = snapshot.child("address").getValue<String>()
                val noTelp = snapshot.child("phone").getValue<String>()
                val gender = snapshot.child("jk").getValue<String>()
                Log.d("Nama Lengkap", "$name")
                binding.editTextEmail.setText(name)
                binding.editTextPassword.setText(password)
                binding.editTextFullName.setText(nama)
                binding.editTextDateOfBirth.setText(tanggalLahir)
                binding.editTextAddress.setText(alamat)
                binding.editTextPhoneNumber.setText(noTelp)
                if (image != null){
                    Glide.with(requireContext()).load(image).into(binding.imageViewProfile)
                }else{
                    binding.imageViewProfile.setImageResource(R.drawable.user)

                }
                
                val radioButtonMale = binding.radio1
                val radioButtonFemale = binding.radio2

                // Initialize the selected gender based on the retrieved value from the database
                if (gender == "Laki-laki") {
                    radioButtonMale.isChecked = true
                    selectedGender = "Laki-laki"
                } else if (gender == "Perempuan") {
                    radioButtonFemale.isChecked = true
                    selectedGender = "Perempuan"
                }
            }
        }

        storageReference = FirebaseStorage.getInstance().reference.child("profile_images")

        binding.imageViewProfile.setOnClickListener {
            startGallery()
        }

        binding.buttonSave.setOnClickListener {
            if (getFile != null) {
                val fullname = binding.editTextFullName.text.toString()
                val date = binding.editTextDateOfBirth.text.toString()
                val pass = binding.editTextPassword.text.toString()
                val phone = binding.editTextPhoneNumber.text.toString()
                val email = binding.editTextEmail.text.toString()
                val address = binding.editTextAddress.text.toString()
                val jk = selectedGender.toString()

                if (fullname.isEmpty() || date.isEmpty() || pass.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || jk.isEmpty()) {
                    Toast.makeText(requireContext(), "Harap isi semua data!!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val imageFileName = "${auth.currentUser?.uid}_${System.currentTimeMillis()}.jpg"
                val imageRef = storageReference.child(imageFileName)
                imageRef.putFile(imageUri)
                    .addOnSuccessListener { taskSnapshot ->
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val image = uri.toString()
                            val user = User(fullname, date, pass, phone, email, address, image, jk)
                            saveUserProfile(user)
                        }.addOnFailureListener { exception ->
                            Toast.makeText(requireContext(), "Periksa kembali format file gambar!", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(requireContext(), "Periksa kembali format file gambar!", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Harap isi semua data!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Gambar!")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                imageUri = uri
                val myFile = uriToFile(uri, requireContext())
                getFile = myFile
                binding.imageViewProfile.setImageURI(uri)
            }
        }
    }

    private fun saveUserProfile(user: User) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            databaseReference.child(uid).setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Berhasil Update",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Gagal update profil",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }
}
