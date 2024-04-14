package com.example.habittracker.repository

import com.example.habittracker.data.models.Habit
import com.example.habittracker.db.HabitDB
import java.util.UUID

class HabitRepository(private val habitDB: HabitDB) {
    fun insertHabit(habit: Habit) {
        habitDB.getDao().insert(habit)
    }

    fun deleteHabit(habit: Habit) {
        habitDB.getDao().delete(habit)
    }

    fun updateHabit(habit: Habit) {
        habitDB.getDao().update(habit)
    }

    fun getAllHabits() = habitDB.getDao().getAllHabits()

    fun findById(uuid: UUID): Habit {
        return habitDB.getDao().findById(uuid)
    }
}