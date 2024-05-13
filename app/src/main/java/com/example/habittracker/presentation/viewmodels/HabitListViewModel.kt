package com.example.habittracker.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.local.entity.HabitEntity
import com.example.habittracker.data.local.entity.HabitType
import com.example.habittracker.data.repository.HabitRepository
import kotlinx.coroutines.launch

class HabitListViewModel(
    private val habitRepository: HabitRepository
) : ViewModel() {

    var habitsLiveData: LiveData<List<HabitEntity>> = habitRepository.getAllHabits()

    private var currentList: List<HabitEntity> = listOf()

    private val _filterByLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val filterByLiveData: LiveData<Boolean> = _filterByLiveData

    private val _searchNameLiveData: MutableLiveData<String> = MutableLiveData()
    val searchNameLiveData: LiveData<String> = _searchNameLiveData

    init {
        importHabitsFromApi()
        _filterByLiveData.value = false
        _searchNameLiveData.value = ""
    }

    fun getHabitsByType(habitType: HabitType): List<HabitEntity> {
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

    private fun importHabitsFromApi() {
        viewModelScope.launch {
            habitRepository.importHabitsFromApi()
        }
    }

    fun setCurrentList(newList: List<HabitEntity>) {
        currentList = newList
    }

    fun deleteHabit(habitEntity: HabitEntity) = viewModelScope.launch {
        habitRepository.deleteHabit(habitEntity)
    }

    fun createHabit(habitEntity: HabitEntity) = viewModelScope.launch {
        habitRepository.insertHabit(habitEntity)
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
}