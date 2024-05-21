package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.remote.dto.HabitDTO
import com.example.domain.model.Habit
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import java.util.Calendar
import java.util.UUID

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var editDate: Long = Calendar.getInstance().timeInMillis,
    var name: String,
    var description: String,
    var priority: HabitPriority,
    var type: HabitType,
    var executionQuantity: Int,
    var frequency: Int,
    val doneDates: List<Long>,
    var color: Float,
) {
    fun toHabitDto(): HabitDTO {
        return HabitDTO(
            id = id.toString(),
            editDate = editDate,
            name = name,
            description = description,
            priority = priority.value,
            type = type.value,
            executionQuantity = executionQuantity,
            frequency = frequency,
            color = color.toInt(),
            doneDates = doneDates
        )
    }

    fun toHabit(): Habit {
        return Habit(
            id = id.toString(),
            editDate = editDate,
            name = name,
            description = description,
            priority = priority,
            type = type,
            executionQuantity = executionQuantity,
            frequency = frequency,
            color = color,
        )
    }

    override fun toString(): String {
        return "|$name - $type - $id|"
    }
}


