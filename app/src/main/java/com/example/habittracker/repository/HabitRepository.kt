package com.example.habittracker.repository

import com.example.habittracker.data.models.Habit
import com.example.habittracker.db.HabitDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class HabitRepository(private val habitDB: HabitDB) {
    suspend fun insertHabit(habit: Habit) = withContext(Dispatchers.IO) {
        habitDB.getDao().insert(habit)
    }

    suspend fun deleteHabit(habit: Habit) = withContext(Dispatchers.IO) {
        habitDB.getDao().delete(habit)
    }

    suspend fun updateHabit(habit: Habit) = withContext(Dispatchers.IO) {
        habitDB.getDao().update(habit)
    }

    fun getAllHabits() = habitDB.getDao().getAllHabits()

    suspend fun getHabitById(uuid: UUID): Habit {
        return withContext(Dispatchers.IO) {
            habitDB.getDao().getHabitById(uuid)
        }
    }
}