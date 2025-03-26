package com.example.sazakyanapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import com.example.sazakyanapp.profile.MainProfileActivity
import com.example.sazakyanapp.profile.VerificationActivity
import com.example.sazakyanapp.walkthrough.SecondWT
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var nextBtn: Button
    private lateinit var skipBtn: TextView
    private lateinit var smsVerificationBtn: Button
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Optional: splash screen for 1.5 seconds
        Thread.sleep(1500)
        installSplashScreen()

        setContentView(R.layout.activity_main)

        // Hook up UI elements
        nextBtn = findViewById(R.id.btnNext)
        skipBtn = findViewById(R.id.skipButton)


        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Navigation menu clicks
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
                R.id.nav_share -> Toast.makeText(applicationContext, "Clicked Share", Toast.LENGTH_SHORT).show()
            }
            true
        }

        // Button actions
        nextBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, SecondWT::class.java))
            overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit)
        }

        skipBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainProfileActivity::class.java))
            overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
