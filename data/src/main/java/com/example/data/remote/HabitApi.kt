package com.example.data.remote

import com.example.data.remote.dto.DoneHabitDto
import com.example.data.remote.dto.HabitDTO
import com.example.data.remote.dto.UUIDDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT


interface HabitApi {
    @GET("habit")
    suspend fun getHabitsList(): Response<List<HabitDTO>>

    @PUT("habit")
    suspend fun putHabit(
        @Body habitDTO: HabitDTO
    ): Response<UUIDDto>

    @POST("habit_done")
    suspend fun doneHabit(
        @Body doneHabitDto: DoneHabitDto
    ): Response<Unit>

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(
        @Body uidDTO: UUIDDto
    ): Response<Unit>
}

