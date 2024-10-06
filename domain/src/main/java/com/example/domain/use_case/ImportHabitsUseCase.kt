package com.example.domain.use_case

import com.example.domain.repository.HabitRepository
import javax.inject.Inject

class ImportHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke() {
        return habitRepository.importHabits()
    }
}