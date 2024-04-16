package com.example.habittracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.models.Habit
import com.example.habittracker.data.models.HabitType
import com.example.habittracker.repository.HabitRepository
import kotlinx.coroutines.launch

private const val TAG = "VM HabitList"

class HabitListViewModel(
    private val habitRepository: HabitRepository
) : ViewModel() {

    var habitsLiveData: LiveData<List<Habit>> = habitRepository.getAllHabits()

    private var currentList: List<Habit> = listOf()

    private val _filterByLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val filterByLiveData: LiveData<Boolean> = _filterByLiveData

    private val _searchNameLiveData: MutableLiveData<String> = MutableLiveData()
    val searchNameLiveData: LiveData<String> = _searchNameLiveData

    init {
        _filterByLiveData.value = true
        _searchNameLiveData.value = ""
    }

    fun getHabitsByType(habitType: HabitType): List<Habit> {
        val list = currentList.filter { it.type == habitType }
        val searchString = searchNameLiveData.value.toString().trim()

        return if (_filterByLiveData.value == true) {
            list.sortedBy { it.editDate }
                .filter { it.name.lowercase().contains(searchString.lowercase()) }
        } else {
            list.sortedByDescending { it.editDate }
                .filter { it.name.lowercase().contains(searchString.lowercase()) }
        }
    }

    fun updateLiveData() {
        habitsLiveData = habitRepository.getAllHabits()
    }

    fun filterByAsc() {
        _filterByLiveData.value = true
    }

    fun filterByDesc() {
        _filterByLiveData.value = false
    }

    fun setSearchingName(name: String) {
        _searchNameLiveData.value = name
    }

    fun setCurrentList(newList: List<Habit>) {
        currentList = newList
    }

    fun deleteHabit(habit: Habit) = viewModelScope.launch {
        habitRepository.deleteHabit(habit)
    }

    fun createHabit(habit: Habit) = viewModelScope.launch {
        habitRepository.insertHabit(habit)
    }
}
