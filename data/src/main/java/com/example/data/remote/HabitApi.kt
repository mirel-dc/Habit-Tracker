package com.example.data.remote

import com.example.data.remote.dto.HabitDTO
import com.example.data.remote.dto.UUIDDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.PUT


interface HabitApi {
    @GET("habit")
    @Headers("Authorization: ", TOKEN)
    suspend fun getHabitsList(): Response<List<HabitDTO>>

    @PUT("habit")
    @Headers("Authorization: ", TOKEN)
    suspend fun putHabit(
        @Body habitDTO: HabitDTO
    ): Response<UUIDDto>

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    @Headers("Authorization: ", TOKEN)
    suspend fun deleteHabit(
        @Body uidDTO: UUIDDto
    ): Response<Unit>

    companion object {
        const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
        const val TOKEN = "5fed0393-afec-4259-933c-d847f281c8fe"
    }
}

