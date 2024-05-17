package com.example.data.local.entity

import com.example.data.R

enum class HabitPriorityEntity(val value: Int) {
    HIGH(0),
    MEDIUM(1),
    LOW(2);

    companion object {
        fun getResourceIdByValue(priority: HabitPriorityEntity): Int {
            return when (priority) {
                HIGH -> R.string.high
                MEDIUM -> R.string.medium
                LOW -> R.string.low
            }
        }

        fun getStringIdByValue(value: Int): Int {
            return when (value) {
                0 -> R.string.high
                1 -> R.string.medium
                else -> R.string.low
            }
        }
    }
}