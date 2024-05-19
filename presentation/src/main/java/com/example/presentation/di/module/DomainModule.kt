package com.example.presentation.di.module

import com.example.domain.repository.HabitRepository
import com.example.domain.use_case.DeleteHabitUseCase
import com.example.domain.use_case.GetHabitByIdUseCase
import com.example.domain.use_case.GetHabitsUseCase
import com.example.domain.use_case.ImportHabitsUseCase
import com.example.domain.use_case.InsertHabitUseCase
import com.example.domain.use_case.UpdateHabitUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideDeleteHabitUseCase(habitRepository: HabitRepository): DeleteHabitUseCase {
        return DeleteHabitUseCase(habitRepository = habitRepository)
    }

    @Provides
    fun provideGetHabitByIdUseCase(habitRepository: HabitRepository): GetHabitByIdUseCase {
        return GetHabitByIdUseCase(habitRepository = habitRepository)
    }

    @Provides
    fun provideGetHabitsUseCase(habitRepository: HabitRepository): GetHabitsUseCase {
        return GetHabitsUseCase(habitRepository = habitRepository)
    }

    @Provides
    fun provideImportHabitsUseCase(habitRepository: HabitRepository): ImportHabitsUseCase {
        return ImportHabitsUseCase(habitRepository = habitRepository)
    }

    @Provides
    fun provideInsertHabitUseCase(habitRepository: HabitRepository): InsertHabitUseCase {
        return InsertHabitUseCase(habitRepository = habitRepository)
    }

    @Provides
    fun provideUpdateHabitUseCase(habitRepository: HabitRepository): UpdateHabitUseCase {
        return UpdateHabitUseCase(habitRepository = habitRepository)
    }
}