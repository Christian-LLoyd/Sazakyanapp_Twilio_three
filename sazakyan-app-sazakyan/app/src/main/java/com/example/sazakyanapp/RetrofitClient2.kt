package com.example.sazakyanapp.network

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient2 {
    private const val BASE_URL = "https://verify.twilio.com/v2/"

    // NEW Twilio Credentials for SMS (replace these with your new values)
    private const val ACCOUNT_SID = ""    // e.g., "ACxxxxxxxxxxxxxxxxxxxxx"
    private const val AUTH_TOKEN = ""    // e.g., "xxxxxxxxxxxxxxxxxxxxxx"

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

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TwilioApiService by lazy {
        retrofit.create(TwilioApiService::class.java)
    }
}
