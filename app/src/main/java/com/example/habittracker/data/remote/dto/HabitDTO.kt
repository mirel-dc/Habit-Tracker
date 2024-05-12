package com.example.habittracker.data.remote.dto

import com.example.habittracker.data.local.entity.HabitEntity
import com.example.habittracker.data.local.entity.HabitType
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class HabitDTO(
    @SerializedName("uid")
    var id: String?,
    @SerializedName("date")
    var editDate: Long,
    @SerializedName("title")
    var name: String,
    var description: String,
    var priority: Int,
    var type: Int,
    @SerializedName("count")
    var executionQuantity: Int,
    var frequency: Int,
    var color: Int,
) {
    fun toHabitEntity(): HabitEntity {
        return HabitEntity(
            id = UUID.fromString(id),
            editDate = editDate,
            name = name,
            description = description,
            priority = priority,
            type = HabitType.getFromApiId(type),
            executionQuantity = executionQuantity,
            frequency = frequency,
            color = color.toFloat()
        )
    }
}