package com.example.contractsdemo.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.contractsdemo.R
import com.example.contractsdemo.data.UserData
import com.example.contractsdemo.databinding.ActivityOptionsBinding
import com.example.contractsdemo.databinding.ActivitySignInBinding
import com.example.contractsdemo.databinding.DialogRecoverPasswordBinding
import com.example.contractsdemo.ui.dashboard.DashboardActivity
import com.example.contractsdemo.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignInActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var dialogRecoverPasswordBinding: DialogRecoverPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userRef = database.reference.child("users")

        binding.apply {
            tvSignup.setOnClickListener {
                val intent = Intent(this@SignInActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            btnLogin.setOnClickListener {
                val email = etEmail.editText.text.toString().trim()
                val password = etPassword.editText.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@SignInActivity, "Please enter email and password", Toast.LENGTH_SHORT).show()
                } else {
                    loginUser(email, password)
                }
            }

            tvForget.setOnClickListener {
                showRecoverPasswordDialog()
            }
        }

    }

    private fun showRecoverPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Recover Password")

        dialogRecoverPasswordBinding = DialogRecoverPasswordBinding.inflate(LayoutInflater.from(this@SignInActivity))
        builder.setView(dialogRecoverPasswordBinding.root)

        builder.setPositiveButton("Recover") { dialog, which ->
            val email = dialogRecoverPasswordBinding.etEmail.editText.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your registered email address", Toast.LENGTH_SHORT).show()
            } else {
                recoverPassword(email)
            }
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun recoverPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent. Please check your email.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to send password reset email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    user?.uid?.let { userId ->
                        userRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    val userData = dataSnapshot.getValue(UserData::class.java)
                                    // userData contains the retrieved user data from the Realtime Database
                                    if (userData != null) {
                                        // Do something with the user data
                                        Toast.makeText(this@SignInActivity, "Login successful", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this@SignInActivity, DashboardActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Toast.makeText(this@SignInActivity, "Failed to fetch user data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}