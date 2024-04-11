package com.example.habittracker.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.repository.HabitRepository

class HabitListViewModelFactory(private val habitRepository: HabitRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HabitRepository::class.java).newInstance(habitRepository)
    }
}