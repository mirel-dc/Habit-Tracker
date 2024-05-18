package com.example.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TAG = "HabitApi"

private const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
private const val TOKEN = "5fed0393-afec-4259-933c-d847f281c8fe"

//class RetrofitInstance {
//    companion object {
//        private val retrofit by lazy {
//            val loggingInterceptor =
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//
//            val tokenInterceptor = Interceptor { chain ->
//                val originalRequest = chain.request()
//
//                val modifiedRequest = originalRequest.newBuilder()
//                    .header("Authorization", TOKEN)
//                    .build()
//
//                chain.proceed(modifiedRequest)
//            }
//
//            val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
//                .addInterceptor(loggingInterceptor)
//                .addInterceptor(tokenInterceptor)
//                .build()
//
//            Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build()
//        }
//
//        val service by lazy {
//            retrofit.create(HabitApi::class.java)
//        }
//    }
//}
