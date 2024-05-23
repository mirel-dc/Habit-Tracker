package com.example.presentation

import android.os.IBinder
import android.os.Parcel
import android.os.Parcelable
import android.view.WindowManager
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.presentation.fragments.CreateHabitFragment
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
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

        // Сохранение привычки
        onView(withId(R.id.btnSubmit)).perform(click())

        // Проверка успешного сохранения
        onView(withText("Habit successfully created")).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }
}

class ToastMatcher() : TypeSafeMatcher<Root>() {

    constructor(parcel: Parcel) : this() {
    }

    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    public override fun matchesSafely(root: Root): Boolean {
        val type = root.windowLayoutParams.get().type
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            val windowToken: IBinder = root.decorView.windowToken
            val appToken: IBinder = root.decorView.applicationWindowToken
            if (windowToken === appToken) {
                return true
            }
        }
        return false
    }

    companion object CREATOR : Parcelable.Creator<ToastMatcher> {
        override fun createFromParcel(parcel: Parcel): ToastMatcher {
            return ToastMatcher(parcel)
        }

        override fun newArray(size: Int): Array<ToastMatcher?> {
            return arrayOfNulls(size)
        }
    }
}