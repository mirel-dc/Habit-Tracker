package com.example.domain.model

data class Habit(
    var id: String,
    var editDate: Long,
    var name: String,
    var description: String,
    var priority: HabitPriority,
    var type: HabitType,
    var executionQuantity: Int,
    var frequency: Int,
    var color: Float,
)
