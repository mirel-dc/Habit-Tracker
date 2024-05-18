package com.example.domain.use_case

import com.example.domain.model.Habit
import com.example.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        return habitRepository.deleteHabit(habit)
    }
}