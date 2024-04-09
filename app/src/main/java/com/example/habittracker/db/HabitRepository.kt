package com.example.habittracker.db

import com.example.habittracker.data.models.Habit

class HabitRepository (private val habitDB: HabitDB){
    fun insertHabit(habit: Habit){
        habitDB.getDao().insertHabit(habit)
    }

    fun getAllHabits() = habitDB.getDao().getAllHabits()
}