package com.example.presentation.utils

import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import com.example.presentation.R

object GetResIdFromEnum {
    fun fromType(habitType: HabitType): Int {
        return when (habitType) {
            HabitType.GOOD -> R.string.good_habit
            HabitType.BAD -> R.string.bad_habit
        }
    }

    fun fromPriority(habitPriority: HabitPriority): Int {
        return when (habitPriority) {
            HabitPriority.HIGH -> R.string.high
            HabitPriority.MEDIUM -> R.string.medium
            HabitPriority.LOW -> R.string.low
        }
    }
}