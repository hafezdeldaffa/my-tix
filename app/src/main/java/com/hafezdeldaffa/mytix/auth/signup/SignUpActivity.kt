package com.hafezdeldaffa.mytix.auth.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import com.hafezdeldaffa.mytix.R
import com.hafezdeldaffa.mytix.auth.signin.User

class SignUpActivity : AppCompatActivity() {
    lateinit var vUsername: String
    lateinit var vPassword: String
    lateinit var vName: String
    lateinit var vEmail: String
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mDatabase =
            FirebaseDatabase.getInstance("https://my-tix-8b99f-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("User")
        val btnNext = findViewById<Button>(R.id.btnNext)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)

        btnNext.setOnClickListener {
            vUsername = etUsername.text.toString()
            vPassword = etPassword.text.toString()
            vName = etName.text.toString()
            vEmail = etEmail.text.toString()

            if (vUsername.equals("")) {
                etUsername.error = "Please add your username"
                etUsername.requestFocus()
            } else if (vPassword.equals("")) {
                etPassword.error = "Please add your password"
                etPassword.requestFocus()
            } else if (vName.equals("")) {
                etName.error = "Please add your name"
                etName.requestFocus()
            } else if (vEmail.equals("")) {
                etEmail.error = "Please add your email address"
            } else {
                saveUser(vUsername, vPassword, vName, vEmail)
            }
        }
    }

    private fun saveUser(vUsername: String, vPassword: String, vName: String, vEmail: String) {
        val user = User()
        user.username = vUsername
        user.password = vPassword
        user.name = vName
        user.email = vEmail

        checkingUsername(vUsername, user)
    }

    private fun checkingUsername(vUsername: String, data: User) {
        mDatabase.child(vUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user == null) {
                    mDatabase.child(vUsername).setValue(data)

                    val intent =
                        Intent(
                            this@SignUpActivity,
                            SignUpPhotoActivity::class.java
                        ).putExtra("name", data.name)
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Username is already taken",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}