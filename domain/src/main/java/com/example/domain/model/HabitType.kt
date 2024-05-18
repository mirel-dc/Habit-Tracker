package com.example.domain.model

enum class HabitType(val value: Int) {
    GOOD(0),
    BAD(1);

    companion object {
        fun fromValue(value: Int): HabitType {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("No enum HabitType constant with value $value")
        }
    }
}