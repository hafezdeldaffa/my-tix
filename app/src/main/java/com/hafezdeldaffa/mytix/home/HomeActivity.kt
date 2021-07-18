package com.hafezdeldaffa.mytix.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.hafezdeldaffa.mytix.R

class HomeActivity : AppCompatActivity() {
    lateinit var ivHome: ImageView
    lateinit var ivTicket: ImageView
    lateinit var ivProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val fragmentHome = DashboardFragment()
        val fragmentTicket = TicketFragment()
        val fragmentProfile = ProfileFragment()

        ivHome = findViewById(R.id.ivMenu1)
        ivTicket = findViewById(R.id.ivMenu2)
        ivProfile = findViewById(R.id.ivMenu3)

        ivHome.setOnClickListener {
            setFragment(fragmentHome)

            changeIcon(ivHome, R.drawable.ic_home_active)
            changeIcon(ivTicket, R.drawable.ic_tiket)
            changeIcon(ivProfile, R.drawable.ic_profile)
        }

        ivTicket.setOnClickListener {
            setFragment(fragmentTicket)

            changeIcon(ivHome, R.drawable.ic_home)
            changeIcon(ivTicket, R.drawable.ic_tiket_active)
            changeIcon(ivProfile, R.drawable.ic_profile)
        }

        ivProfile.setOnClickListener {
            setFragment(fragmentProfile)

            changeIcon(ivHome, R.drawable.ic_home)
            changeIcon(ivTicket, R.drawable.ic_tiket)
            changeIcon(ivProfile, R.drawable.ic_profile_active)
        }
    }

    private fun setFragment (fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_frame, fragment)
        fragmentTransaction.commit()
    }

    private fun changeIcon(imageView: ImageView, int: Int) {
        imageView.setImageResource(int)
    }
}