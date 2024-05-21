package com.example.data.remote.dto

import com.example.data.local.entity.HabitEntity
import com.example.domain.model.Habit
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import java.util.Calendar
import java.util.UUID

object Mapper {
    fun fromHabitToHabitEntity(habit: Habit): HabitEntity {
        return HabitEntity(
            id = UUID.fromString(habit.id),
            editDate = habit.editDate,
            name = habit.name,
            description = habit.description,
            priority = HabitPriority.fromValue(habit.priority.value),
            type = HabitType.fromValue(habit.type.value),
            executionQuantity = habit.executionQuantity,
            frequency = habit.frequency,
            color = habit.color,
        )
    }

    fun fromHabitToHabitDto(habit: Habit): HabitDTO {
        return HabitDTO(
            id = habit.id,
            editDate = habit.editDate,
            name = habit.name,
            description = habit.description,
            priority = habit.priority.value,
            type = habit.type.value,
            executionQuantity = habit.executionQuantity,
            frequency = habit.frequency,
            color = habit.color.toInt(),
        )
    }

    fun fromHabitToDoneHabitDto(habit: Habit): DoneHabitDto {
        return DoneHabitDto(
            date = (Calendar.getInstance().timeInMillis),
            habitUid = habit.id
        )
    }
}