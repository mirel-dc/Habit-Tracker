package com.example.domain.use_case

import com.example.domain.model.Habit
import com.example.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabitById @Inject constructor(
    private val habitRepository: HabitRepository
) {
    operator fun invoke(uuid: String): Habit {
        return habitRepository.getHabitById(uuid)
    }
}