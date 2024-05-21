package com.example.domain.repository

import com.example.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getHabits(): Flow<List<Habit>>

    suspend fun importHabits()

    suspend fun insertHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

    suspend fun getHabitById(uuid: String): Habit

    suspend fun deleteHabit(habit: Habit)

    fun completeHabit(habit: Habit): Flow<String>
}