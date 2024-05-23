import com.example.domain.repository.HabitRepository
import com.example.domain.use_case.ImportHabitsUseCase
import com.example.habittracker.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class ImportHabitsUseCaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var importHabitsUseCase: ImportHabitsUseCase
    private val fakeHabitRepo: HabitRepository = mock()

    @Before
    fun setUp() {
        importHabitsUseCase = ImportHabitsUseCase(fakeHabitRepo)
    }

    @Test
    fun `invoke should call importHabits on habitRepository`() = runTest {

        // Arrange
        // No setup needed


        // Act
        importHabitsUseCase()


        // Assert
        verify(fakeHabitRepo).importHabits()
    }
}