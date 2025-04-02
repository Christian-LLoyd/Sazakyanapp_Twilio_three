package com.example.sazakyanapp.profile

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sazakyanapp.HomeActivity
import com.example.sazakyanapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBtn: Button
    private lateinit var editUser: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var loginEmail: TextInputEditText
    private lateinit var loginContact: TextInputEditText
    private lateinit var databaseHelper: Database

    // Removed: private lateinit var smsVerificationBtn: Button

    // Firebase Database Reference
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance("https://sazakyan-app-676dd-default-rtdb.asia-southeast1.firebasedatabase.app")
        userRef = database.getReference("users")

        // Debug: Log database reference
        Log.d("FirebaseDB", "Database Reference: ${database.reference}")

        // Initialize UI elements
        loginBtn = findViewById(R.id.button4)
        editUser = findViewById(R.id.editTextPersonName2)
        editPassword = findViewById(R.id.editTextPassword3)
        loginEmail = findViewById(R.id.editTextEmail2)
        //loginContact = findViewById(R.id.editContactNumber2)
        // Removed: smsVerificationBtn = findViewById(R.id.btnSmsVerification)
        databaseHelper = Database(this)

        loginBtn.setOnClickListener {
            val insertedUser = editUser.text.toString()
            val insertedPass = editPassword.text.toString()
            val insertedEmail = loginEmail.text.toString()
            val insertedContact = "0"

            // Log user input
            Log.d("UserInput", "Username: $insertedUser, Password: $insertedPass, Email: $insertedEmail, Contact: $insertedContact")

            if (TextUtils.isEmpty(insertedUser) || TextUtils.isEmpty(insertedPass)) {
                Toast.makeText(this, "Add Username & Password!", Toast.LENGTH_SHORT).show()
            } else {
                val checkUser = databaseHelper.checkuserpass(insertedUser, insertedEmail, insertedContact, insertedPass)
                if (checkUser) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                    // Write user data to Firebase Database
                    val userData = mapOf(
                        "username" to insertedUser,
                        "email" to insertedEmail,
                        "contact" to insertedContact,
                        "password" to insertedPass
                    )

                    userRef.child(insertedUser).setValue(userData)
                        .addOnSuccessListener {
                            Log.d("FirebaseDB", "User data added successfully")

                            // Immediately check if data exists
                            userRef.child(insertedUser).get().addOnSuccessListener { snapshot ->
                                if (snapshot.exists()) {
                                    Log.d("FirebaseDB", "Data Retrieved: ${snapshot.value}")
                                } else {
                                    Log.e("FirebaseDB", "Data was not saved!")
                                }
                            }.addOnFailureListener { e ->
                                Log.e("FirebaseDB", "Failed to retrieve data", e)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirebaseDB", "Failed to add data", e)
                        }

                    // Navigate to HomeActivity
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    intent.putExtra("username", insertedUser)
                    intent.putExtra("email", insertedEmail)
                    intent.putExtra("contact", insertedContact)
                    intent.putExtra("password", insertedPass)
                    startActivity(intent)
                    overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit)
                } else {
                    Toast.makeText(this, "Wrong Credentials!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<TextView>(R.id.signupRedirect).setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit)
        }

        // Removed:
        // smsVerificationBtn.setOnClickListener {
        //     startActivity(Intent(this, VerificationActivity::class.java))
        // }
    }
}
