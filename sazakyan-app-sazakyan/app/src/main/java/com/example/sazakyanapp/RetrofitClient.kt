package com.example.sazakyanapp.network

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://verify.twilio.com/v2/"

    // Twilio Credentials (Replace with your actual values)
    private const val ACCOUNT_SID = "ACf639c448361fbb5192ad3af91799b3b7"
    private const val AUTH_TOKEN = "bcc4baf6912a51f53a890e2341a0ba14"

    private val client = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Authorization", Credentials.basic(ACCOUNT_SID, AUTH_TOKEN))
                .build()
            chain.proceed(request)
        }
        addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }) // Logging
    }.build()

    val api: TwilioApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TwilioApiService::class.java)
}
