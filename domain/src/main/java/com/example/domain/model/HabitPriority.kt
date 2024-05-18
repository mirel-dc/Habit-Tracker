package com.example.domain.model

enum class HabitPriority(val value: Int) {
    HIGH(0),
    MEDIUM(1),
    LOW(2);

    companion object {
        fun fromValue(value: Int): HabitPriority {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("No enum HabitPriority constant with value $value")
        }
    }
}