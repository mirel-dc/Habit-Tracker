package com.example.presentation

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.presentation.fragments.CreateHabitFragment
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateHabitFragmentTest : TestCase() {

    private lateinit var scenario: FragmentScenario<CreateHabitFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_HabitTracker)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testCreateHabit() {
        //Открытие экрана создания привычки
        onView(withId(R.id.fabCreateHabit)).perform(click())

        // Ввод данных привычки
        onView(withId(R.id.etName)).perform(typeText("Test Habit"), closeSoftKeyboard())
        onView(withId(R.id.etDescription)).perform(
            typeText("Test Description"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.etFrequency)).perform(typeText("3"), closeSoftKeyboard())
        onView(withId(R.id.etExecutionQuantity)).perform(typeText("5"), closeSoftKeyboard())

        // Выбор приоритета и типа привычки
        onView(withId(R.id.spPriority)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("High"))).perform(click())

        onView(withId(R.id.rgHabitType)).check(matches(isDisplayed()))
        onView(withId(R.id.rbGood)).perform(click())
    }
}
