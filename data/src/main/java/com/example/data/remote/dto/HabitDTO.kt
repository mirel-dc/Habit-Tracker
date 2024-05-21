package com.example.data.remote.dto

import com.example.data.local.entity.HabitEntity
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
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
    @SerializedName("done_dates")
    val doneDates: List<Long>,
    var frequency: Int,
    var color: Int,
) {
    fun toHabitEntity(): HabitEntity {
        return HabitEntity(
            id = UUID.fromString(id),
            editDate = editDate,
            name = name,
            description = description,
            priority = HabitPriority.fromValue(priority),
            type = HabitType.fromValue(type),
            executionQuantity = executionQuantity,
            frequency = frequency,
            color = color.toFloat()
        )
    }
}
