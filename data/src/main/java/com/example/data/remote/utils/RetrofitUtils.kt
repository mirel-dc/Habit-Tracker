package com.example.data.remote.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitUtils {
    const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
    private const val TOKEN = "5fed0393-afec-4259-933c-d847f281c8fe"


    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val tokenInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", TOKEN)
            .build()

        chain.proceed(modifiedRequest)
    }

    val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(tokenInterceptor)
        .build()

    val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
}