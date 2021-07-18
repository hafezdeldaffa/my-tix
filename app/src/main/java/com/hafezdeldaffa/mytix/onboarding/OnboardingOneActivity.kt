package com.hafezdeldaffa.mytix.onboarding

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.hafezdeldaffa.mytix.R
import com.hafezdeldaffa.mytix.auth.signin.SignInActivity
import com.hafezdeldaffa.mytix.utils.Preferences

class OnboardingOneActivity : AppCompatActivity() {
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        val buttonNext = findViewById<Button>(R.id.btnNext)
        val buttonSkip = findViewById<Button>(R.id.btnSkip)
        preferences = Preferences(this)

        if (preferences.getValues("onboarding").equals("1")) {
            finishAffinity()
            val intent = Intent(this@OnboardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        buttonNext.setOnClickListener {
            val intent = Intent(this@OnboardingOneActivity, OnboardingTwoActivity::class.java)
            startActivity(intent)
        }

        buttonSkip.setOnClickListener {
            finishAffinity()
            val intent = Intent(this@OnboardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}