package com.example.domain.use_case

import com.example.domain.model.Habit
import com.example.domain.model.HabitCountState
import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompleteHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    operator fun invoke(habit: Habit): Flow<HabitCountState> {
        return habitRepository.completeHabit(habit)
    }
}