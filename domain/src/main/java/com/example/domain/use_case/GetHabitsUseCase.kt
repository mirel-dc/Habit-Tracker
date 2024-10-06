package com.example.domain.use_case

import com.example.domain.model.Habit
import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    operator fun invoke(): Flow<List<Habit>> {
        return habitRepository.getHabits()
    }
}