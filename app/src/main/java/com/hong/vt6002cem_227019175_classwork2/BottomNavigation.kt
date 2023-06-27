package com.hong.vt6002cem_227019175_classwork2

import CompassFragment
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hong.vt6002cem_227019175_classwork2.fragment.*;

class BottomNavigation : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())

                    true
                }

                R.id.map -> {
                    loadFragment(MapFragment())
                    true
                }
                R.id.compass -> {
                    loadFragment(CompassFragment())
                    true
                }
                R.id.fsitem -> {
                    loadFragment(FengShuiItemFragment())
                    true
                }
                else -> {
                    true
                }
            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }


}