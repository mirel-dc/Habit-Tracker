package com.example.habittracker.domain.use_case

import com.example.domain.model.Habit
import com.example.domain.model.HabitCountState
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import com.example.domain.repository.HabitRepository
import com.example.domain.use_case.CompleteHabitUseCase
import com.example.habittracker.utils.MainCoroutineRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CompleteHabitUseCaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var completeHabitUseCase: CompleteHabitUseCase
    private val fakeHabitRepo: HabitRepository = mock()

    @Before
    fun setUp() {
        completeHabitUseCase = CompleteHabitUseCase(fakeHabitRepo)
    }

    @Test
    fun `invoke should return KEEP_DOING for GOOD habit when current count is less than max`() =
        runTest {

            // Arrange
            val habit = Habit(
                id = "uuid",
                editDate = 0,
                name = "TestName1",
                description = "TestDescription1",
                type = HabitType.GOOD,
                priority = HabitPriority.LOW,
                doneDates = listOf(),
                frequency = 2,
                executionQuantity = 0,
                color = 0f
            )
            whenever(fakeHabitRepo.completeHabit(habit))
                .thenReturn(flow {
                    emit(
                        getState(
                            habitType = habit.type,
                            currentCount = habit.executionQuantity,
                            maxCount = habit.frequency
                        )
                    )
                })

            // Act
            val result = completeHabitUseCase(habit)


            // Assert
            launch {
                result.collect { state ->
                    assertEquals(HabitCountState.KEEP_DOING, state)
                }
            }

            verify(fakeHabitRepo, times(1)).completeHabit(habit)
        }

    @Test
    fun `invoke should return URE_BREATHTAKING for GOOD habit when current count is more than max`() =
        runTest {

            // Arrange
            val habit = Habit(
                id = "uuid",
                editDate = 0,
                name = "TestName1",
                description = "TestDescription1",
                type = HabitType.GOOD,
                priority = HabitPriority.LOW,
                doneDates = listOf(),
                frequency = 2,
                executionQuantity = 2,
                color = 0f
            )
            whenever(fakeHabitRepo.completeHabit(habit))
                .thenReturn(flow {
                    emit(
                        getState(
                            habitType = habit.type,
                            currentCount = habit.executionQuantity,
                            maxCount = habit.frequency
                        )
                    )
                })


            // Act
            val result = completeHabitUseCase(habit)
            advanceUntilIdle()


            // Assert
            launch {
                result.collect { state ->
                    assertEquals(HabitCountState.URE_BREATHTAKING, state)
                }
            }

            verify(fakeHabitRepo, times(1)).completeHabit(habit)
        }

    @Test
    fun `invoke should return STOP_IT for BAD habit when current count is less than max`() =
        runTest {

            // Arrange
            val habit = Habit(
                id = "uuid",
                editDate = 0,
                name = "TestName1",
                description = "TestDescription1",
                type = HabitType.BAD,
                priority = HabitPriority.LOW,
                doneDates = listOf(),
                frequency = 2,
                executionQuantity = 0,
                color = 0f
            )
            whenever(fakeHabitRepo.completeHabit(habit))
                .thenReturn(flow {
                    emit(
                        getState(
                            habitType = habit.type,
                            currentCount = habit.executionQuantity,
                            maxCount = habit.frequency
                        )
                    )
                })


            // Act
            val result = completeHabitUseCase(habit)
            advanceUntilIdle()


            // Assert
            launch {
                result.collect { state ->
                    assertEquals(HabitCountState.STOP_IT, state)
                }
            }

            verify(fakeHabitRepo, times(1)).completeHabit(habit)
        }

    @Test
    fun `invoke should return NO_MORE for BAD habit when current count is less than max`() =
        runTest {

            // Arrange
            val habit = Habit(
                id = "uuid",
                editDate = 0,
                name = "TestName1",
                description = "TestDescription1",
                type = HabitType.BAD,
                priority = HabitPriority.LOW,
                doneDates = listOf(),
                frequency = 2,
                executionQuantity = 2,
                color = 0f
            )
            whenever(fakeHabitRepo.completeHabit(habit))
                .thenReturn(flow {
                    emit(
                        getState(
                            habitType = habit.type,
                            currentCount = habit.executionQuantity,
                            maxCount = habit.frequency
                        )
                    )
                })


            // Act
            val result = completeHabitUseCase(habit)
            advanceUntilIdle()


            // Assert
            launch {
                result.collect { state ->
                    assertEquals(HabitCountState.NO_MORE, state)
                }
            }

            verify(fakeHabitRepo, times(1)).completeHabit(habit)
        }


    private fun getState(habitType: HabitType, currentCount: Int, maxCount: Int): HabitCountState {
        return when (habitType) {
            HabitType.GOOD -> {
                if (currentCount < maxCount) {
                    HabitCountState.KEEP_DOING
                } else {
                    HabitCountState.URE_BREATHTAKING
                }
            }

            HabitType.BAD -> {
                if (currentCount < maxCount) {
                    HabitCountState.STOP_IT
                } else {
                    HabitCountState.NO_MORE
                }
            }
        }

    }
}