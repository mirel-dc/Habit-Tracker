package com.example.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habitEntity: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun importHabits(habitEntities: List<HabitEntity>)

    @Update
    suspend fun update(habitEntity: HabitEntity)

    @Delete
    suspend fun delete(habitEntity: HabitEntity)

    @Query("DELETE FROM habits")
    suspend fun nukeHabits()

    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: UUID): HabitEntity
}