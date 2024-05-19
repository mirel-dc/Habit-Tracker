package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.db.HabitDB
import com.example.data.local.db.HabitDao
import com.example.data.remote.HabitApi
import com.example.data.repository.HabitRepositoryImpl
import com.example.domain.repository.HabitRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class DataModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): HabitDB {
        return Room.databaseBuilder(context, HabitDB::class.java, "Habit.db").build()
    }

    @Provides
    @Singleton
    fun provideHabitDao(database: HabitDB): HabitDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun provideHabitRepository(habitApi: HabitApi, habitDao: HabitDao): HabitRepository {
        return HabitRepositoryImpl(api = habitApi, dao = habitDao)
    }
}