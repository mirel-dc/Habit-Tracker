package com.example.presentation.di.module

import com.example.domain.repository.HabitRepository
import com.example.domain.use_case.CompleteHabitUseCase
import com.example.domain.use_case.DeleteHabitUseCase
import com.example.domain.use_case.FilterAndSearchHabitsUseCase
import com.example.domain.use_case.GetHabitByIdUseCase
import com.example.domain.use_case.GetHabitsUseCase
import com.example.domain.use_case.ImportHabitsUseCase
import com.example.domain.use_case.InsertHabitUseCase
import com.example.domain.use_case.UpdateHabitUseCase
import com.example.domain.use_case.ValidateHabitUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {
    @Provides
    @Singleton
    fun provideDeleteHabitUseCase(habitRepository: HabitRepository): DeleteHabitUseCase {
        return DeleteHabitUseCase(habitRepository = habitRepository)
    }

    @Provides
    @Singleton
    fun provideGetHabitByIdUseCase(habitRepository: HabitRepository): GetHabitByIdUseCase {
        return GetHabitByIdUseCase(habitRepository = habitRepository)
    }

    @Provides
    @Singleton
    fun provideGetHabitsUseCase(habitRepository: HabitRepository): GetHabitsUseCase {
        return GetHabitsUseCase(habitRepository = habitRepository)
    }

    @Provides
    @Singleton
    fun provideImportHabitsUseCase(habitRepository: HabitRepository): ImportHabitsUseCase {
        return ImportHabitsUseCase(habitRepository = habitRepository)
    }

    @Provides
    @Singleton
    fun provideInsertHabitUseCase(habitRepository: HabitRepository): InsertHabitUseCase {
        return InsertHabitUseCase(habitRepository = habitRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateHabitUseCase(habitRepository: HabitRepository): UpdateHabitUseCase {
        return UpdateHabitUseCase(habitRepository = habitRepository)
    }

    @Provides
    @Singleton
    fun provideCompleteHabitUseCase(habitRepository: HabitRepository): CompleteHabitUseCase {
        return CompleteHabitUseCase(habitRepository = habitRepository)
    }

    @Provides
    @Singleton
    fun provideFilterAndSearchHabitsUseCase(): FilterAndSearchHabitsUseCase {
        return FilterAndSearchHabitsUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateHabitUseCase(): ValidateHabitUseCase {
        return ValidateHabitUseCase()
    }
}