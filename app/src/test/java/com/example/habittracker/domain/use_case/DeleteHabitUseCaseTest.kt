package com.example.habittracker.domain.use_case

import com.example.domain.model.Habit
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import com.example.domain.repository.HabitRepository
import com.example.domain.use_case.CompleteHabitUseCase
import com.example.domain.use_case.DeleteHabitUseCase
import com.example.habittracker.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class DeleteHabitUseCaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var deleteHabitUseCase : DeleteHabitUseCase
    private val fakeHabitRepo: HabitRepository = mock()

    @Before
    fun setUp() {
        deleteHabitUseCase = DeleteHabitUseCase(fakeHabitRepo)
    }

    @Test
    fun `DeleteHabitUseCase invoking repository delete`() = runTest {

        // Arrange
        val testHabit = Habit(
            id = "GoodId",
            editDate = 123124123,
            name = "TestName1",
            description = "TestDescription1",
            type = HabitType.GOOD,
            priority = HabitPriority.LOW,
            doneDates = listOf(),
            frequency = 0,
            executionQuantity = 0,
            color = 0f
        )
        whenever(fakeHabitRepo.deleteHabit(testHabit)).thenReturn(Unit)


        // Act
        launch {
            val actual = deleteHabitUseCase.invoke(testHabit)
        }
        advanceUntilIdle()

        //Assert
        Mockito.verify(fakeHabitRepo, Mockito.times(1)).deleteHabit(testHabit)
    }
}