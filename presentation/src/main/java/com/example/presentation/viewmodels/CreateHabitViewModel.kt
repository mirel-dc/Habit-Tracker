package com.example.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.HabitEntity
import com.example.data.remote.dto.Mapper
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import com.example.domain.use_case.GetHabitByIdUseCase
import com.example.domain.use_case.InsertHabitUseCase
import com.example.domain.use_case.UpdateHabitUseCase
import com.example.domain.use_case.ValidateHabitUseCase
import com.example.presentation.R
import com.example.presentation.utils.GetResIdFromEnum
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CreateHabitViewModel"

class CreateHabitViewModel @Inject constructor(
    private val insertHabitUseCase: InsertHabitUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val validateHabitUseCase: ValidateHabitUseCase,
) : ViewModel() {
    var currentHabit = MutableLiveData<HabitEntity>()
        private set

    private var isUpdate = false

    var priorities =
        HabitPriority.entries.toTypedArray().map { habitPriority ->
            GetResIdFromEnum.fromPriority(habitPriority)
        }
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
            priority = HabitPriority.HIGH,
            executionQuantity = 0,
            color = 0f,
            type = HabitType.GOOD,
            frequency = 0,
            doneDates = listOf()
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
                insertHabitUseCase(it.toHabit())
            }
        }
    }

    private fun updateHabit() = viewModelScope.launch {
        currentHabit.value.let {
            if (it != null) {
                updateHabitUseCase(it.toHabit())
            }
        }
    }

    fun setCurrentHabitWithUUID(habitUUID: String) = viewModelScope.launch {
        val habit = async {
            getHabitByIdUseCase(habitUUID)
        }
        currentHabit.value = Mapper.fromHabitToHabitEntity(habit.await())
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


    //Легко расширить и добавить различные ошибки
    //например возвращать енам с состояниями, или использовать Resource
    fun validateName(enteredName: String) {
        if (!validateHabitUseCase.validateName(enteredName))
            _nameError.value = R.string.cannot_be_empty
        else
            _nameError.value = null
    }

    fun validateDescription(enteredDescription: String) {
        if (!validateHabitUseCase.validateDescription(enteredDescription))
            _descriptionError.value = R.string.cannot_be_empty
        else
            _descriptionError.value = null
    }

    fun validateQuantity(enteredQuantity: String) {
        if (!validateHabitUseCase.validateQuantity(enteredQuantity))
            _quantityError.value = R.string.cannot_be_empty
        else
            _quantityError.value = null
    }

    fun validateFrequency(enteredFrequency: String) {
        if (!validateHabitUseCase.validateFrequency(enteredFrequency))
            _frequencyError.value = R.string.cannot_be_empty
        else
            _frequencyError.value = null
    }

    private fun isValid(): Boolean {
        return (validateHabitUseCase.isValid(
            name = currentHabit.value?.name.toString(),
            frequency = currentHabit.value?.frequency.toString(),
            quantity = currentHabit.value?.executionQuantity.toString(),
            description = currentHabit.value?.description.toString(),
        ))
    }
}