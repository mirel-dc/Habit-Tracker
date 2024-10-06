package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DoneHabitDto(
    @SerializedName("date")
    val date: Long,
    @SerializedName("habit_uid")
    val habitUid: String
)