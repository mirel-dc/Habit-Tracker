package com.example.domain.repository

import com.example.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getHabits() : Flow<List<Habit>>

    fun insertHabit(habit: Habit)

    fun updateHabit(habit: Habit)

    fun getHabitById(uuid : String) : Habit
}