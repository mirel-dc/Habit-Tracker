import com.example.domain.model.Habit
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import com.example.domain.repository.HabitRepository
import com.example.domain.use_case.UpdateHabitUseCase
import com.example.habittracker.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class UpdateHabitUseCaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var updateHabitUseCase: UpdateHabitUseCase
    private val fakeHabitRepo: HabitRepository = mock()

    @Before
    fun setUp() {
        updateHabitUseCase = UpdateHabitUseCase(fakeHabitRepo)
    }

    @Test
    fun `invoke should call updateHabit on habitRepository`() = runTest {

        // Arrange
        val habit = Habit(
            id = "1",
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


        // Act
        updateHabitUseCase(habit)

        // Assert
        verify(fakeHabitRepo).updateHabit(habit)
    }
}