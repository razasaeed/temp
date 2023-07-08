package com.example.contractsdemo.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.contractsdemo.R
import com.example.contractsdemo.databinding.ActivityOptionsBinding
import com.example.contractsdemo.databinding.ActivityRegisterBinding
import com.example.contractsdemo.databinding.ActivitySignInBinding
import com.example.contractsdemo.ui.signin.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.apply {
            tvLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, SignInActivity::class.java)
                startActivity(intent)
            }
            btnRegister.setOnClickListener {
                val name = etName.editText.text.toString().trim()
                val email = etEmail.editText.text.toString().trim()
                val password = etPassword.editText.text.toString().trim()
                val cnic = etCnic.editText.text.toString().trim()

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || cnic.isEmpty()) {
                    Toast.makeText(this@RegisterActivity, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                } else {
                    registerUser(name, email, password, cnic)
                }
            }
        }

    }

    private fun registerUser(name: String, email: String, password: String, cnic: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val userRef = database.reference.child("users").child(userId ?: "")

                    val userData = HashMap<String, Any>()
                    userData["name"] = name
                    userData["email"] = email
                    userData["cnic"] = cnic

                    userRef.setValue(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity, SignInActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}