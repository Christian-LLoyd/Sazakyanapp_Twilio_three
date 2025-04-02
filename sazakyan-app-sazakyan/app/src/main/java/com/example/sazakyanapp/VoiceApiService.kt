package com.example.sazakyanapp.network

import com.example.sazakyanapp.models.VoiceCallResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface VoiceApiService {
    @FormUrlEncoded
    @POST("Calls.json")
    fun makeCall(
        @Field("From") from: String,
        @Field("To") to: String,
        @Field("Url") url: String
    ): Call<VoiceCallResponse>
}
