package com.example.habittracker.presentation.viewmodels

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.R
import com.example.habittracker.data.local.entity.HabitEntity
import com.example.habittracker.data.local.entity.HabitType
import com.example.habittracker.data.repository.HabitRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "CreateHabitViewModel"

class CreateHabitViewModel(
    private val habitRepository: HabitRepository
) : ViewModel() {
    var currentHabit = MutableLiveData<HabitEntity>()
        private set

    private var isUpdate = false

    var priorities = arrayOf(1, 2, 3)
        private set

    private val _nameError = MutableLiveData<Int?>()
    val nameError: LiveData<Int?> = _nameError

    private val _descriptionError = MutableLiveData<Int?>()
    val descriptionError: LiveData<Int?> = _descriptionError

    private val _frequencyError = MutableLiveData<Int?>()
    val frequencyError: LiveData<Int?> = _frequencyError

    private val _quantityError = MutableLiveData<Int?>()
    val quantityError: LiveData<Int?> = _quantityError

    private val toastChannel = Channel<Int>()
    val toastFlow = toastChannel.receiveAsFlow()

    init {
        emptyCurrentHabit()
    }

    fun emptyCurrentHabit() {
        currentHabit.value = HabitEntity(
            name = "",
            description = "",
            priority = 0,
            executionQuantity = 0,
            color = 0f,
            type = HabitType.GOOD,
            frequency = 0
        )
        isUpdate = false
    }

    //Returning true for popBackStack if all fine
    fun submitBtnAction(): Boolean {
        return if (isValid()) {
            if (isUpdate) {
                updateHabit()
            } else {
                createHabit()
            }
            true
        } else {
            viewModelScope.launch {
                toastChannel.send(R.string.incorrectly_filled_fields)
            }
            false
        }
    }

    private fun createHabit() = viewModelScope.launch {
        currentHabit.value.let {
            if (it != null) {
                habitRepository.insertHabit(it)
            }
        }
    }

    private fun updateHabit() = viewModelScope.launch {
        currentHabit.value.let {
            if (it != null) {
                habitRepository.updateHabit(it)
            }
        }
    }

    fun setCurrentHabitWithUUID(habitUUID: String?) = viewModelScope.launch {
        val habit = async { habitRepository.getHabitById(UUID.fromString(habitUUID)) }
        currentHabit.value = habit.await()
        isUpdate = true
        initValidationErrors()
    }

    fun initValidationErrors() {
        if (!isUpdate) {
            _nameError.value = R.string.cannot_be_empty
            _descriptionError.value = R.string.cannot_be_empty
            _quantityError.value = R.string.cannot_be_empty
            _frequencyError.value = R.string.cannot_be_empty
        } else {
            _nameError.value = null
            _descriptionError.value = null
            _quantityError.value = null
            _frequencyError.value = null
        }
    }

    fun validateName(enteredName: String): Boolean {
        return if (TextUtils.isEmpty(enteredName)) {
            _nameError.value = R.string.cannot_be_empty
            false
        } else {
            _nameError.value = null
            true
        }
    }

    fun validateDescription(enteredName: String): Boolean {
        return if (TextUtils.isEmpty(enteredName)) {
            _descriptionError.value = R.string.cannot_be_empty
            false
        } else {
            _descriptionError.value = null
            true
        }
    }

    fun validateQuantity(enteredQuantity: String): Boolean {
        return if (enteredQuantity == "") {
            _quantityError.value = R.string.cannot_be_empty
            false
        } else {
            _quantityError.value = null
            true
        }
    }

    fun validateFrequency(enteredFrequency: String): Boolean {
        return if (enteredFrequency == "") {
            _frequencyError.value = R.string.cannot_be_empty
            false
        } else {
            _frequencyError.value = null
            true
        }
    }


    private fun isValid(): Boolean {
        return (validateFrequency(currentHabit.value?.frequency.toString())
                && validateName(currentHabit.value?.name.toString())
                && validateQuantity(currentHabit.value?.executionQuantity.toString())
                && validateDescription(currentHabit.value?.description.toString()))
    }
}