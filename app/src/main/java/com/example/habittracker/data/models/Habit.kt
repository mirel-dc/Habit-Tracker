package com.example.habittracker.data.models

import com.example.habittracker.adapters.HabitAdapter

data class Habit(
    var name: String?,
    var description: String?,
    var priority: Int,
    var type: HabitAdapter.HabitType,
    var executionQuantity: Int,
    var frequency: Int,
    var color: Float
)