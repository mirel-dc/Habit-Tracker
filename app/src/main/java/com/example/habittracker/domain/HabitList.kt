package com.example.habittracker.domain

import com.example.habittracker.data.models.Habit
import com.example.habittracker.data.models.HabitType
import java.util.UUID

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

    fun updateHabit(existingHabit: Habit) {
        habitList.find { it.id == existingHabit.id }?.apply {
            name = existingHabit.name
            description = existingHabit.description
            frequency = existingHabit.frequency
            type = existingHabit.type
            executionQuantity = existingHabit.executionQuantity
            priority = existingHabit.priority
            color = existingHabit.color
        }
    }

    fun getHabitByUUID(habitUUID: UUID?): Habit? {
        return habitList.find { it.id == habitUUID }
    }
}