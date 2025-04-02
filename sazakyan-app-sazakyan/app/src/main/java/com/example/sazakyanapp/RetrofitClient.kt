package com.example.sazakyanapp.network

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://verify.twilio.com/v2/"


    private const val ACCOUNT_SID = ""
    private const val AUTH_TOKEN = ""

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
