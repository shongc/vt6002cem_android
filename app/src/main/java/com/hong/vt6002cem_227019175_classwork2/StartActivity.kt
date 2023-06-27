package com.hong.vt6002cem_227019175_classwork2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class StartActivity : AppCompatActivity() {
    val handler = Handler();

    val runnable = Runnable {
        val intent = Intent(this@StartActivity, BottomNavigation::class.java)
        startActivity(intent)


        setContentView(R.layout.activity_bottom_navigation)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        handler.postDelayed(runnable, 3000)

    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}