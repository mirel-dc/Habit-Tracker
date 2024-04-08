package com.example.habittracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.data.models.Habit
import com.example.habittracker.data.models.HabitType
import com.example.habittracker.domain.HabitList

private const val TAG = "VM HabitList"

class HabitListViewModel : ViewModel() {

    private val _habitsLiveData: MutableLiveData<List<Habit>> = MutableLiveData()
    val habitsLiveData: LiveData<List<Habit>> = _habitsLiveData

    private val _filterByLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val filterByLiveData: LiveData<Boolean> = _filterByLiveData

    private val _searchNameLiveData: MutableLiveData<String> = MutableLiveData()
    val searchNameLiveData: LiveData<String> = _searchNameLiveData

    init {
        importHabits()
        _filterByLiveData.value = true
        _searchNameLiveData.value = ""
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

    private fun importHabits() {
        _habitsLiveData.value = HabitList.getHabits()
    }

    fun getHabitsByType(habitType: HabitType): List<Habit> {
        val list = _habitsLiveData.value?.filter { it.type == habitType } ?: listOf()
        val searchString = searchNameLiveData.value.toString().trim()

        return if (_filterByLiveData.value == true) {
            list.sortedBy { it.editDate }
                .filter { it.name.lowercase().contains(searchString.lowercase()) }
        } else {
            list.sortedByDescending { it.editDate }
                .filter { it.name.lowercase().contains(searchString.lowercase()) }
        }
    }
}
