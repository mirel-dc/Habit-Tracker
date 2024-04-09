package com.example.habittracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.db.HabitRepository

class HabitListViewModelFactory(private val habitRepository: HabitRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HabitRepository::class.java).newInstance(habitRepository)
    }
}