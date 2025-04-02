package com.example.sazakyanapp.network

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient3 {
    // Base URL for Twilio Voice API. We append the ACCOUNT_SID below.
    private const val BASE_URL = "https://api.twilio.com/2010-04-01/Accounts/"

    // Twilio Credentials for Voice API (replace these with your actual values)
    private const val ACCOUNT_SID = ""
    private const val AUTH_TOKEN = ""

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Authorization", Credentials.basic(ACCOUNT_SID, AUTH_TOKEN))
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    // The base URL is composed by appending the ACCOUNT_SID and a trailing slash.
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("$BASE_URL$ACCOUNT_SID/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Create an instance of our Voice API service.
    val api: VoiceApiService by lazy {
        retrofit.create(VoiceApiService::class.java)
    }
}
