package com.example.sazakyanapp.profile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sazakyanapp.R
import com.example.sazakyanapp.network.RetrofitClient2
import com.example.sazakyanapp.models.TwilioResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallVerificationActivity : AppCompatActivity() {

    private lateinit var phoneNumberEditText: EditText
    private lateinit var codeEditText: EditText
    private lateinit var sendCallButton: Button
    private lateinit var verifyCodeButton: Button
    private lateinit var progressBar: ProgressBar

    private val verifyServiceSid = "VA41a8372e31d0a7b144c526aaf27a2879"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_verification)

        // Initialize UI components (IDs must match those in XML)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        codeEditText = findViewById(R.id.codeEditText)
        sendCallButton = findViewById(R.id.sendCallButton)
        verifyCodeButton = findViewById(R.id.verifyCodeButton)
        progressBar = findViewById(R.id.progressBar)

        sendCallButton.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                sendVerificationCode(phoneNumber)
            } else {
                Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
            }
        }

        verifyCodeButton.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            val code = codeEditText.text.toString().trim()
            if (phoneNumber.isNotEmpty() && code.isNotEmpty()) {
                verifyCode(phoneNumber, code)
            } else {
                Toast.makeText(this, "Enter verification code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        progressBar.visibility = View.VISIBLE

        val call = RetrofitClient2.api.sendOTP(
            serviceSid = verifyServiceSid,
            recipient = phoneNumber,
            channel = "call"
        )

        call.enqueue(object : Callback<TwilioResponse> {
            override fun onResponse(call: Call<TwilioResponse>, response: Response<TwilioResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@CallVerificationActivity, "Call initiated, enter the code", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@CallVerificationActivity, "Failed to send call verification", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TwilioResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@CallVerificationActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun verifyCode(phoneNumber: String, code: String) {
        progressBar.visibility = View.VISIBLE

        val call = RetrofitClient2.api.verifyOTP(
            serviceSid = verifyServiceSid,
            recipient = phoneNumber,
            code = code
        )

        call.enqueue(object : Callback<TwilioResponse> {
            override fun onResponse(call: Call<TwilioResponse>, response: Response<TwilioResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body()?.status == "approved") {
                    Toast.makeText(this@CallVerificationActivity, "Verification successful!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@CallVerificationActivity, "Invalid verification code", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TwilioResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@CallVerificationActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
