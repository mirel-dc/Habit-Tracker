import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.data.local.entity.HabitEntity
import com.example.domain.model.Habit
import com.example.domain.model.HabitCountState
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import com.example.domain.use_case.CompleteHabitUseCase
import com.example.domain.use_case.DeleteHabitUseCase
import com.example.domain.use_case.FilterAndSearchHabitsUseCase
import com.example.domain.use_case.GetHabitsUseCase
import com.example.domain.use_case.ImportHabitsUseCase
import com.example.domain.use_case.InsertHabitUseCase
import com.example.presentation.utils.GetResIdFromEnum
import com.example.presentation.viewmodels.HabitListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.UUID

@ExperimentalCoroutinesApi
class HabitListViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HabitListViewModel

    private val deleteHabitUseCase: DeleteHabitUseCase = mock()
    private val getHabitsUseCase: GetHabitsUseCase = mock()
    private val insertHabitUseCase: InsertHabitUseCase = mock()
    private val importHabitsUseCase: ImportHabitsUseCase = mock()
    private val completeHabitUseCase: CompleteHabitUseCase = mock()
    private val filterAndSearchHabitsUseCase: FilterAndSearchHabitsUseCase = mock()

    private val testHabitEntity = HabitEntity(
        id = UUID.randomUUID(),
        editDate = 0,
        name = "TestName1",
        description = "TestDescription1",
        type = HabitType.GOOD,
        priority = HabitPriority.LOW,
        doneDates = listOf(),
        frequency = 0,
        executionQuantity = 1,
        color = 0f
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Default)
        whenever(getHabitsUseCase()).thenReturn(flow { emit(emptyList<Habit>()) })
        viewModel = HabitListViewModel(
            deleteHabitUseCase,
            getHabitsUseCase,
            insertHabitUseCase,
            importHabitsUseCase,
            completeHabitUseCase,
            filterAndSearchHabitsUseCase
        )
    }

    @Test
    fun `importHabitsFromApi should call importHabitsUseCase`() = runTest {

        // Act
        viewModel.importHabitsFromApi()


        // Assert
        verify(importHabitsUseCase, times(2)).invoke()
    }

    @Test
    fun `deleteHabit should call deleteHabitUseCase`() = runTest {

        // Act
        viewModel.deleteHabit(testHabitEntity)


        // Assert
        verify(deleteHabitUseCase).invoke(testHabitEntity.toHabit())
    }

    @Test
    fun `createHabit should call insertHabitUseCase`() = runTest {

        // Act
        viewModel.createHabit(testHabitEntity)


        // Assert
        verify(insertHabitUseCase).invoke(testHabitEntity.toHabit())
    }

    @Test
    fun `getHabitsByType should call filterAndSearchHabitsUseCase`() {
        // Arrange
        val habitEntities = listOf(testHabitEntity)
        val habitType = HabitType.GOOD
        viewModel.setCurrentList(habitEntities)
        filterAndSearchHabitsUseCase.filterAndSearch(
            habits = habitEntities.map {
                it.toHabit()
            },
            habitType = HabitType.GOOD,
            searchString = "",
            isAsc = false
        )
        whenever(
            filterAndSearchHabitsUseCase.filterAndSearch(
                habits = habitEntities.map {
                    it.toHabit()
                },
                habitType = HabitType.GOOD,
                searchString = "",
                isAsc = false
            )
        ).thenReturn(listOf(testHabitEntity.toHabit()))


        // Act
        val result = viewModel.getHabitsByType(habitType)


        // Assert
        assert(result == habitEntities)
    }

    @Test
    fun `filterByAsc should set filterByLiveData to true`() {

        // Act
        viewModel.filterByAsc()


        // Assert
        assert(viewModel.filterByLiveData.value == true)
    }

    @Test
    fun `filterByDesc should set filterByLiveData to false`() {

        // Act
        viewModel.filterByDesc()


        // Assert
        assert(viewModel.filterByLiveData.value == false)
    }

    @Test
    fun `setSearchingName should set searchNameLiveData to given name`() {

        // Arrange
        val name = "test"


        // Act
        viewModel.setSearchingName(name)


        // Assert
        assert(viewModel.searchNameLiveData.value == name)
    }
}