package com.example.sazakyanapp.network

import retrofit2.Call
import retrofit2.http.*
import com.example.sazakyanapp.models.TwilioResponse

interface TwilioApiService {
    // Send OTP via SMS, Email, or Call
    @FormUrlEncoded
    @POST("Services/{serviceSid}/Verifications")
    fun sendOTP(
        @Path("serviceSid") serviceSid: String,
        @Field("To") recipient: String, // phone number or email address
        @Field("Channel") channel: String // "sms", "email", or "call"
    ): Call<TwilioResponse>

    // Verify the OTP (for SMS, Email, or Call)
    @FormUrlEncoded
    @POST("Services/{serviceSid}/VerificationCheck")
    fun verifyOTP(
        @Path("serviceSid") serviceSid: String,
        @Field("To") recipient: String,
        @Field("Code") code: String
    ): Call<TwilioResponse>
}
