package com.example.sazakyanapp.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sazakyanapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.sazakyanapp.models.TwilioResponse
import com.example.sazakyanapp.network.RetrofitClient2


class VerificationActivity : AppCompatActivity() {

    private lateinit var phoneNumberEditText: EditText
    private lateinit var codeEditText: EditText
    private lateinit var sendCodeButton: Button
    private lateinit var verifyCodeButton: Button
    private lateinit var CallVerificationButton: Button
    private lateinit var EmailVerificationButton: Button

    private val verifyServiceSid = "VA8b2a34986f51560d8102d6167afe4e10" // Replace with your actual SID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        codeEditText = findViewById(R.id.codeEditText)
        sendCodeButton = findViewById(R.id.sendCodeButton)
        verifyCodeButton = findViewById(R.id.verifyCodeButton)
        CallVerificationButton = findViewById(R.id.btntocallverification)
        EmailVerificationButton = findViewById(R.id.btntoEmailverification)


        // Send Verification Code
        sendCodeButton.setOnClickListener {
            var phoneNumber = phoneNumberEditText.text.toString().trim()

            // Format number (Add +63 if not included)
            phoneNumber = formatPhoneNumber(phoneNumber)

            if (isValidPhoneNumber(phoneNumber)) {
                sendVerificationCode(phoneNumber)
            } else {
                Toast.makeText(this, "Invalid phone number. Enter a valid 10-15 digit number.", Toast.LENGTH_SHORT).show()
            }
        }

        CallVerificationButton.setOnClickListener {
            startActivity(Intent(this, CallVerificationActivity::class.java))
        }

        EmailVerificationButton.setOnClickListener{
            startActivity(Intent(this, EmailVerificationActivity::class.java))
        }

        // Verify OTP Code
        verifyCodeButton.setOnClickListener {
            val phoneNumber = formatPhoneNumber(phoneNumberEditText.text.toString().trim())
            val code = codeEditText.text.toString().trim()
            if (phoneNumber.isNotEmpty() && code.isNotEmpty()) {
                checkVerificationCode(phoneNumber, code)
            } else {
                Toast.makeText(this, "Enter phone number & code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Back Button method
    fun onBackButtonClicked(view: android.view.View) {
        onBackPressed()  // This will navigate back to the previous screen
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.matches(Regex("^\\+?\\d{10,15}$")) // Allow numbers with or without "+"
    }

    private fun formatPhoneNumber(phone: String): String {
        return if (!phone.startsWith("+")) {
            "+63" + phone.removePrefix("0") // Add country code for PH and remove leading 0
        } else {
            phone // Return as-is if already in international format
        }
    }

    private fun sendVerificationCode(recipient: String) {
        val call = RetrofitClient2.api.sendOTP(
            serviceSid = verifyServiceSid,
            recipient = recipient,  // Use recipient instead of phoneNumber
            channel = "sms"
        )
        call.enqueue(object : Callback<TwilioResponse> {
            override fun onResponse(call: Call<TwilioResponse>, response: Response<TwilioResponse>) {
                if (response.isSuccessful) {
                    val twilioResp = response.body()
                    Log.d("Twilio", "Send OTP success. sid=${twilioResp?.sid}, status=${twilioResp?.status}")
                    Toast.makeText(this@VerificationActivity, "Verification code sent!", Toast.LENGTH_SHORT).show()
                } else {
                    handleError(response, "Error sending code")
                }
            }

            override fun onFailure(call: Call<TwilioResponse>, t: Throwable) {
                handleFailure(t)
            }
        })
    }

    private fun checkVerificationCode(recipient: String, code: String) {
        val call = RetrofitClient2.api.verifyOTP(
            serviceSid = verifyServiceSid,
            recipient = recipient,  // Use recipient instead of phoneNumber
            code = code
        )
        call.enqueue(object : Callback<TwilioResponse> {
            override fun onResponse(call: Call<TwilioResponse>, response: Response<TwilioResponse>) {
                if (response.isSuccessful) {
                    val twilioResp = response.body()
                    Log.d("Twilio", "Verify OTP success. sid=${twilioResp?.sid}, status=${twilioResp?.status}")

                    when (twilioResp?.status) {
                        "approved" -> {
                            Toast.makeText(this@VerificationActivity, "Code is correct!", Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        }
                        "pending" -> {
                            Toast.makeText(this@VerificationActivity, "Verification pending. Try again.", Toast.LENGTH_SHORT).show()
                        }
                        "expired" -> {
                            Toast.makeText(this@VerificationActivity, "Code expired. Request a new one.", Toast.LENGTH_SHORT).show()
                        }
                        "failed" -> {
                            Toast.makeText(this@VerificationActivity, "Invalid code. Check and retry.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this@VerificationActivity, "Unknown response. Try again.", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    handleError(response, "Error verifying code")
                }
            }

            override fun onFailure(call: Call<TwilioResponse>, t: Throwable) {
                handleFailure(t)
            }
        })
    }

    private fun handleError(response: Response<TwilioResponse>, defaultMessage: String) {
        val errorBody = response.errorBody()?.string()
        Log.e("Twilio", "$defaultMessage. Code=${response.code()} Body=$errorBody")
        Toast.makeText(this@VerificationActivity, defaultMessage, Toast.LENGTH_SHORT).show()
    }

    private fun handleFailure(t: Throwable) {
        t.printStackTrace()
        Toast.makeText(this@VerificationActivity, "Network error. Check connection.", Toast.LENGTH_SHORT).show()
    }
}
