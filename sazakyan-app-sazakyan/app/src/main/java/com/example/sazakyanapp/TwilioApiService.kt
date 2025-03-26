package com.example.sazakyanapp.network

import retrofit2.Call
import retrofit2.http.*
import com.example.sazakyanapp.models.TwilioResponse

interface TwilioApiService {

    // 1) Send OTP (SMS or Email)
    @FormUrlEncoded
    @POST("Services/{serviceSid}/Verifications")
    fun sendOTP(
        @Path("serviceSid") serviceSid: String,
        @Field("To") recipient: String, // e.g. "user@example.com"
        @Field("Channel") channel: String // "sms" or "email"
    ): Call<TwilioResponse>

    // 2) Verify OTP (SMS or Email)
    @FormUrlEncoded
    @POST("Services/{serviceSid}/VerificationCheck")
    fun verifyOTP(
        @Path("serviceSid") serviceSid: String,
        @Field("To") recipient: String,
        @Field("Code") code: String
    ): Call<TwilioResponse>
}
