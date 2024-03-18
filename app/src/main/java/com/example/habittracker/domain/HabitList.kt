package com.example.habittracker.domain

import com.example.habittracker.data.models.Habit

object HabitList{
    private var habitList = mutableListOf<Habit>()

    fun getHabits(): MutableList<Habit> {
        return habitList.map { it.copy() }.toMutableList() //Deep copy for diff Util
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