package com.example.habittracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.adapters.HabitAdapter
import com.example.habittracker.adapters.OnRecyclerItemClicked
import com.example.habittracker.data.models.Habit
import com.example.habittracker.databinding.ActivityMainBinding
import com.example.habittracker.domain.HabitList
import com.example.habittracker.utils.SpacingItemDecorator

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private var _adapter: HabitAdapter? = null
    private val adapter
        get() = _adapter ?: throw IllegalStateException("Adapter must not be null")

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                adapter.setNewData(HabitList.getHabits())
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initListTEST()


        val recycler = binding.rvHabit
        _adapter = HabitAdapter(this, clickListener)
        adapter.setData(HabitList.getHabits())
        recycler.adapter = adapter
        recycler.addItemDecoration(SpacingItemDecorator(16))
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.fabCreateHabit.setOnClickListener {
            openCreateHabitActivityForResult()
        }
    }

    private fun doOnClick(habit: Habit) {
        openCreateHabitActivityForResultUpdate(habit)
    }

    private val clickListener = object : OnRecyclerItemClicked {
        override fun onClick(habit: Habit) {
            doOnClick(habit)
        }
    }

    private fun openCreateHabitActivityForResultUpdate(habit: Habit) {
        val intent = Intent(this, CreateHabitActivity::class.java)

        intent.putExtra(CHANGE, true)
        intent.putExtra(NAME, habit.name)
        intent.putExtra(DESCRIPTION, habit.description)
        intent.putExtra(PRIORITY, habit.priority)
        intent.putExtra(TYPE, getString(habit.type.resId))
        intent.putExtra(EXECUTIONQUANTITY, habit.executionQuantity)
        intent.putExtra(FREQUENCY, habit.frequency)
        intent.putExtra(COLOR, habit.color)

        resultLauncher.launch(intent)
    }

    private fun openCreateHabitActivityForResult() {
        val intent = Intent(this, CreateHabitActivity::class.java)
        resultLauncher.launch(intent)
    }


    private fun initListTEST() {
        HabitList.createHabit(
            Habit(
                name = "Выучить Kotlin",
                description = "Выучить Kotlin по гайдам на ютубе",
                type = HabitAdapter.HabitType.LEARN,
                color = 123f,
                priority = 1,
                executionQuantity = 0,
                frequency = 2
            )
        )

        HabitList.createHabit(
            Habit(
                name = "Бегать по утрам",
                description = "Бегать каждое утро в парке под домом",
                type = HabitAdapter.HabitType.SPORT,
                color = 320f,
                priority = 3,
                executionQuantity = 3,
                frequency = 5
            )
        )
    }
}

const val CHANGE = "change"
const val NAME = "name"
const val DESCRIPTION = "description"
const val PRIORITY = "priority"
const val TYPE = "type"
const val EXECUTIONQUANTITY = "executionQuantity"
const val FREQUENCY = "frequency"
const val COLOR = "color"



