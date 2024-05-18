package com.example.data.remote.dto

import com.example.domain.model.Habit

object Mapper {
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
}