import com.example.domain.model.Habit
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import com.example.domain.repository.HabitRepository
import com.example.domain.use_case.InsertHabitUseCase
import com.example.habittracker.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class InsertHabitUseCaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var insertHabitUseCase: InsertHabitUseCase
    private val fakeHabitRepo: HabitRepository = mock()

    @Before
    fun setUp() {
        insertHabitUseCase = InsertHabitUseCase(fakeHabitRepo)
    }

    @Test
    fun `invoke should call insertHabit on habitRepository`() = runTest {

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
        insertHabitUseCase(habit)


        // Assert
        verify(fakeHabitRepo).insertHabit(habit)
    }
}