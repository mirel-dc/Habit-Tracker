package com.example.habittracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.habittracker.data.models.Habit
import com.example.habittracker.fragments.CreateHabitFragment
import com.example.habittracker.fragments.MainPageFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, MainPageFragment())
                .commit()
        }
    }

    fun openCreateFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CreateHabitFragment())
            .addToBackStack(null)
            .commit()
    }

    fun openCreateFragmentWithData(habit : Habit) {
      Log.d("MAIN",habit.toString())
        val fragment = CreateHabitFragment.newInstance(habit)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}


