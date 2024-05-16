package com.example.data.remote.dto

import com.example.data.local.entity.HabitEntity
import com.example.data.local.entity.HabitPriority
import com.example.data.local.entity.HabitType
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
    fun toHabitEntity(): com.example.data.local.entity.HabitEntity {
        return com.example.data.local.entity.HabitEntity(
            id = UUID.fromString(id),
            editDate = editDate,
            name = name,
            description = description,
            priority = com.example.data.local.entity.HabitPriority.getHabitPriorityByValue(priority),
            type = com.example.data.local.entity.HabitType.getHabitTypeByValue(type),
            executionQuantity = executionQuantity,
            frequency = frequency,
            color = color.toFloat()
        )
    }
}