package com.example.data.di

import com.example.data.di.utils.RetrofitUtils
import com.example.data.remote.HabitApi
import com.example.data.remote.gson
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RetrofitUtils.BASE_URL)
            .client(RetrofitUtils.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideHabitApi(retrofit: Retrofit): HabitApi {
        return retrofit.create(HabitApi::class.java)
    }
}