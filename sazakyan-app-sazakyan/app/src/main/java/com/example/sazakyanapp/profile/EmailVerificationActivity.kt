package com.example.sazakyanapp.profile

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.sazakyanapp.R
import com.example.sazakyanapp.models.TwilioResponse
import com.example.sazakyanapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailVerificationActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var codeEditText: EditText
    private lateinit var sendCodeButton: Button
    private lateinit var verifyCodeButton: Button

    // Your Twilio Verify Service SID (Email enabled)
    private val verifyServiceSid = "VA41a8372e31d0a7b144c526aaf27a2879"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verification)

        // 1) Setup custom Toolbar as Action Bar (for back arrow)
        val toolbar = findViewById<Toolbar>(R.id.toolbarEmailVerify)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Email Verification"

        // 2) Hook up UI
        emailEditText = findViewById(R.id.emailEditText)
        codeEditText = findViewById(R.id.codeEditText)
        sendCodeButton = findViewById(R.id.sendCodeButton)
        verifyCodeButton = findViewById(R.id.verifyCodeButton)

        // 3) Send Email Code
        sendCodeButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isNotEmpty()) {
                sendVerificationCode(email)
            } else {
                Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show()
            }
        }

        // 4) Verify Email Code
        verifyCodeButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val code = codeEditText.text.toString().trim()
            if (email.isNotEmpty() && code.isNotEmpty()) {
                verifyEmailCode(email, code)
            } else {
                Toast.makeText(this, "Enter email & code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 5) Handle Action Bar Up (back arrow)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()  // Return to previous screen
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // 6) Send Email Verification Code
    private fun sendVerificationCode(email: String) {
        val call = RetrofitClient.api.sendOTP(
            serviceSid = verifyServiceSid,
            recipient = email,
            channel = "email" // CRUCIAL
        )
        call.enqueue(object : Callback<TwilioResponse> {
            override fun onResponse(call: Call<TwilioResponse>, response: Response<TwilioResponse>) {
                if (response.isSuccessful) {
                    val twilioResp = response.body()
                    Toast.makeText(
                        this@EmailVerificationActivity,
                        "Code sent to $email. status=${twilioResp?.status}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(
                        this@EmailVerificationActivity,
                        "Error sending code: $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<TwilioResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@EmailVerificationActivity,
                    "Network error: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // 7) Verify Email Code
    private fun verifyEmailCode(email: String, code: String) {
        val call = RetrofitClient.api.verifyOTP(
            serviceSid = verifyServiceSid,
            recipient = email,
            code = code
        )
        call.enqueue(object : Callback<TwilioResponse> {
            override fun onResponse(call: Call<TwilioResponse>, response: Response<TwilioResponse>) {
                if (response.isSuccessful) {
                    val twilioResp = response.body()
                    if (twilioResp?.status == "approved") {
                        Toast.makeText(
                            this@EmailVerificationActivity,
                            "Email verified successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed() // or finish()
                    } else {
                        Toast.makeText(
                            this@EmailVerificationActivity,
                            "Verification failed or pending. status=${twilioResp?.status}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(
                        this@EmailVerificationActivity,
                        "Error verifying code: $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<TwilioResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@EmailVerificationActivity,
                    "Network error: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
