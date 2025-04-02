package com.example.sazakyanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.sazakyanapp.profile.CallVerificationActivity
import com.example.sazakyanapp.profile.Database
import com.example.sazakyanapp.profile.EmailVerificationActivity
import com.example.sazakyanapp.profile.LoginActivity
import com.example.sazakyanapp.profile.VerificationActivity
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var imageSlider: ImageSlider
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var database: Database

//    private lateinit var emailVerificationBtn: Button
//    private lateinit var callVerificationBtn: Button
    private lateinit var smsVerificationBtn: Button
//    private lateinit var profileBtn: Button  // Profile button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val artDialogBuilder = AlertDialog.Builder(this)
        val insertedUser = intent.getStringExtra("username")

        database = Database(this)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit)
                    finish()
                }
                R.id.nav_logout -> {
                    artDialogBuilder.setTitle("Log Out")
                        .setMessage("Are you sure you want to sign out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { _, _ ->
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
            true
        }

        // Initialize Buttons dynamically from the layout
//        emailVerificationBtn = findViewById(R.id.btnEmailVerification)
//        callVerificationBtn = findViewById(R.id.btnCallVerification)
        smsVerificationBtn = findViewById(R.id.btnSmsVerificationHome)
//        profileBtn = findViewById(R.id.btnProfile)  // Profile Button

//        emailVerificationBtn.setOnClickListener {
//            startActivity(Intent(this, EmailVerificationActivity::class.java))
//        }
//
//        callVerificationBtn.setOnClickListener {
//            startActivity(Intent(this, CallVerificationActivity::class.java))
//        }

        smsVerificationBtn.setOnClickListener {
            startActivity(Intent(this, VerificationActivity::class.java))
        }

//        profileBtn.setOnClickListener {
//            startActivity(Intent(this, VerificationActivity::class.java))
//        }

        // IMAGE SLIDER
        imageSlider = findViewById(R.id.imageSlider)
        val imagesList = arrayListOf(
            SlideModel(R.drawable.promo_one),
            SlideModel(R.drawable.promo_one),
            SlideModel(R.drawable.promo_one)
        )
        imageSlider.setImageList(imagesList, ScaleTypes.CENTER_INSIDE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }
}
