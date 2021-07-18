package com.hafezdeldaffa.mytix.auth.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import com.hafezdeldaffa.mytix.home.HomeActivity
import com.hafezdeldaffa.mytix.R
import com.hafezdeldaffa.mytix.auth.signup.SignUpActivity
import com.hafezdeldaffa.mytix.utils.Preferences

class SignInActivity : AppCompatActivity() {
    lateinit var iUsername: String
    lateinit var iPassword: String
    lateinit var mDatabase: DatabaseReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)
        mDatabase =
            FirebaseDatabase.getInstance("https://my-tix-8b99f-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("User")
        preferences = Preferences(this)

        preferences.setValues("onboarding", "1")
        if (preferences.getValues("status").equals("1")){
            finishAffinity()

            val home = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(home)
        }

        btnSignIn.setOnClickListener {
            iUsername = etUsername.text.toString()
            iPassword = etPassword.text.toString()

            if (iUsername.equals("")) {
                etUsername.error = "Please add your username!"
                etUsername.requestFocus()
            } else if (iPassword.equals("")) {
                etPassword.error = "Please add your password!"
                etPassword.requestFocus()
            } else {
                pushLogin(iUsername, iPassword)
            }
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)

                if (user == null) {
                    Toast.makeText(this@SignInActivity, "User not found!", Toast.LENGTH_LONG)
                        .show()
                } else {
                    if (user.password.equals(iPassword)) {
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("name", user.name.toString())
                        preferences.setValues("password", user.password.toString())
                        preferences.setValues("url", user.url.toString())
                        preferences.setValues("username", user.username.toString())
                        preferences.setValues("balance", user.balance.toString())
                        preferences.setValues("status", "1")

                        finishAffinity()
                        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignInActivity, "Password is wrong!", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}