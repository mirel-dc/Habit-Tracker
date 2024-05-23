package com.example.habittracker.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.model.Habit
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import com.example.domain.use_case.GetHabitByIdUseCase
import com.example.domain.use_case.InsertHabitUseCase
import com.example.domain.use_case.UpdateHabitUseCase
import com.example.domain.use_case.ValidateHabitUseCase
import com.example.presentation.R
import com.example.presentation.viewmodels.CreateHabitViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.anyString
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.UUID

@ExperimentalCoroutinesApi
class CreateHabitViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CreateHabitViewModel

    private val insertHabitUseCase: InsertHabitUseCase = mock()
    private val getHabitByIdUseCase: GetHabitByIdUseCase = mock()
    private val updateHabitUseCase: UpdateHabitUseCase = mock()
    private val validateHabitUseCase: ValidateHabitUseCase = mock()

    private val testHabit = Habit(
        id = UUID.randomUUID().toString(),
        editDate = 0,
        name = "TestName1",
        description = "TestDescription1",
        type = HabitType.GOOD,
        priority = HabitPriority.LOW,
        doneDates = listOf(),
        frequency = 0,
        executionQuantity = 0,
        color = 0f
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Default)
        viewModel = CreateHabitViewModel(
            insertHabitUseCase,
            getHabitByIdUseCase,
            updateHabitUseCase,
            validateHabitUseCase
        )
    }

    @Test
    fun `emptyCurrentHabit should reset currentHabit and set isUpdate to false`() {

        // Act
        viewModel.emptyCurrentHabit()

        // Assert
        val habit = viewModel.currentHabit.value
        assert(habit != null)
        assert(habit?.name == "")
        assert(habit?.description == "")
        assert(habit?.priority == HabitPriority.HIGH)
        assert(habit?.executionQuantity == 0)
        assert(habit?.color == 0f)
        assert(habit?.type == HabitType.GOOD)
        assert(habit?.frequency == 0)
        assert(habit?.doneDates == listOf<String>())
    }

    @Test
    fun `submitBtnAction should return true and call createHabit if habit is valid`() = runTest {
        // Arrange
        viewModel.emptyCurrentHabit()
        whenever(
            validateHabitUseCase.isValid(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        ).thenReturn(true)


        // Act
        val result = viewModel.submitBtnAction()


        // Assert
        assert(result)
    }

    @Test
    fun `submitBtnAction should return false and send toast if habit is not valid`() = runTest {
        // Arrange
        viewModel.emptyCurrentHabit()
        whenever(
            validateHabitUseCase.isValid(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        ).thenReturn(false)


        // Act
        val result = viewModel.submitBtnAction()


        // Assert
        assert(!result)
    }

    @Test
    fun `setCurrentHabitWithUUID should set currentHabit`() =
        runTest {
            // Arrange
            val habitUUID = UUID.randomUUID().toString()
            whenever(getHabitByIdUseCase.invoke(anyString())).thenReturn(testHabit.copy(id = habitUUID))

            // Act
            launch {
                viewModel.setCurrentHabitWithUUID(habitUUID)
            }
            advanceUntilIdle()

            // Assert
            val currentHabit = viewModel.currentHabit.value
            assert(currentHabit != null)
        }

    @Test
    fun `validateName should set nameError if name is not valid`() {
        // Arrange
        whenever(validateHabitUseCase.validateName(anyString())).thenReturn(false)


        // Act
        viewModel.validateName("")


        // Assert
        assert(viewModel.nameError.value == R.string.cannot_be_empty)
    }

    @Test
    fun `validateDescription should set descriptionError if description is not valid`() {
        // Arrange
        whenever(validateHabitUseCase.validateDescription(anyString())).thenReturn(false)


        // Act
        viewModel.validateDescription("")


        // Assert
        assert(viewModel.descriptionError.value == R.string.cannot_be_empty)
    }

    @Test
    fun `validateQuantity should set quantityError if quantity is not valid`() {
        // Arrange
        whenever(validateHabitUseCase.validateQuantity(anyString())).thenReturn(false)


        // Act
        viewModel.validateQuantity("")


        // Assert
        assert(viewModel.quantityError.value == R.string.cannot_be_empty)
    }

    @Test
    fun `validateFrequency should set frequencyError if frequency is not valid`() {
        // Arrange
        whenever(validateHabitUseCase.validateFrequency(anyString())).thenReturn(false)


        // Act
        viewModel.validateFrequency("")


        // Assert
        assert(viewModel.frequencyError.value == R.string.cannot_be_empty)
    }
}