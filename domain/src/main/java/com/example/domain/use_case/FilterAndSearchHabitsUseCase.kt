package com.example.domain.use_case

import com.example.domain.model.Habit
import com.example.domain.model.HabitType

class FilterAndSearchHabitsUseCase {
    fun filterAndSearch(
        habits: List<Habit>,
        habitType: HabitType,
        searchString: String,
        isAsc: Boolean
    ): List<Habit> {
        val filteredList = habits.filter { it.type == habitType }
        return if (isAsc) {
            filteredList.sortedBy { it.editDate }
                .filter { it.name.lowercase().contains(searchString.lowercase()) }
        } else {
            filteredList.sortedByDescending { it.editDate }
                .filter { it.name.lowercase().contains(searchString.lowercase()) }
        }
    }
}