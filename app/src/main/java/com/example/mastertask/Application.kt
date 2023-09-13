package com.example.mastertask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Application : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application)

        val bottomNav = findViewById<BottomNavigationView>(R.id.navigation_view)
        bottomNav.setOnItemSelectedListener(navListener)

        // as soon as the application opens the first fragment should
        // be shown to the user in this case it is home fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener {
        // By using switch we can easily get the
        // selected fragment by using there id
        lateinit var selectedFragment: Fragment
        when (it.itemId) {
            R.id.home -> {
                selectedFragment = HomeFragment()
            }
            R.id.services -> {
                selectedFragment = ServicesFragment()
            }
            R.id.user -> {
                selectedFragment = UserFragment()
            }
        }
        // It will help to replace the
        // one fragment to other.
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
        true
    }
}