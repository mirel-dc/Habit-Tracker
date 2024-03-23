package com.example.habittracker.domain

import com.example.habittracker.data.models.Habit
import com.example.habittracker.data.models.HabitType

private const val TAG = "HabitList"

object HabitList {
    private var habitList = mutableListOf<Habit>()

    fun getHabits(): List<Habit> {
        return habitList
    }

    fun getHabitByType(habitType: HabitType): List<Habit> {
        return habitList.filter { it.type == habitType }.map { it.copy() }
    }

    fun createHabit(habit: Habit) {
        habitList.add(habit)
    }

    fun updateHabit(
        wasHabitName: String,
        newHabit: Habit
    ) {
        habitList.find { it.name == wasHabitName }?.apply {
            name = newHabit.name
            description = newHabit.description
            frequency = newHabit.frequency
            type = newHabit.type
            executionQuantity = newHabit.executionQuantity
            priority = newHabit.priority
            color = newHabit.color
        }
    }
}