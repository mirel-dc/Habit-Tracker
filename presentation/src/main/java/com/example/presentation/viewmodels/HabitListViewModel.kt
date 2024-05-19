package com.example.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.HabitEntity
import com.example.data.remote.dto.Mapper
import com.example.domain.model.HabitType
import com.example.domain.use_case.DeleteHabitUseCase
import com.example.domain.use_case.GetHabitsUseCase
import com.example.domain.use_case.ImportHabitsUseCase
import com.example.domain.use_case.InsertHabitUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitListViewModel @Inject constructor(
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val getHabitsUseCase: GetHabitsUseCase,
    private val insertHabitUseCase: InsertHabitUseCase,
    private val importHabitsUseCase: ImportHabitsUseCase,
) : ViewModel() {

    var habitsLiveData: LiveData<List<HabitEntity>> =
        getHabitsUseCase().asLiveData().map { habits ->
            habits.map { habit ->
                Mapper.fromHabitToHabitEntity(habit)
            }
        }

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
            importHabitsUseCase()
            //habitRepository.importHabitsFromApi()
        }
    }

    fun setCurrentList(newList: List<HabitEntity>) {
        currentList = newList
    }

    fun deleteHabit(habitEntity: HabitEntity) = viewModelScope.launch {
        deleteHabitUseCase(habitEntity.toHabit())
        //habitRepository.deleteHabit(habitEntity)
    }

    fun createHabit(habitEntity: HabitEntity) = viewModelScope.launch {
        insertHabitUseCase(habitEntity.toHabit())
        //habitRepository.insertHabit(habitEntity)
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