package com.example.habittracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.habittracker.data.models.Habit
import java.util.UUID

@Dao
interface HabitDao {
    @Insert
    fun insertHabit(habit: Habit)

    @Update
    fun update(habit: Habit)

    @Query("SELECT * FROM habits")
    fun getAllHabits(): LiveData<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun findById(id: UUID): Habit

    @Query("DELETE FROM habits")
    fun deleteAllItems()
}