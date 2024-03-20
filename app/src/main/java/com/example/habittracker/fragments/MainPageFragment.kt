package com.example.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.MainActivity
import com.example.habittracker.adapters.HabitAdapter
import com.example.habittracker.adapters.OnRecyclerItemClicked
import com.example.habittracker.data.models.Habit
import com.example.habittracker.data.models.HabitType
import com.example.habittracker.databinding.FragmentMainPageBinding
import com.example.habittracker.domain.HabitList
import com.example.habittracker.utils.SpacingItemDecorator

class MainPageFragment : Fragment() {

    private var _binding: FragmentMainPageBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentMainPage must not be null")

    private var _adapter: HabitAdapter? = null
    private val adapter
        get() = _adapter ?: throw IllegalStateException("Adapter must not be null")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     //   initListTEST()

        val recycler = binding.rvHabit
        _adapter = context?.let { HabitAdapter(it, clickListener) }
        adapter.setData(HabitList.getHabits())
        recycler.adapter = adapter
        recycler.addItemDecoration(SpacingItemDecorator(16))
        recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)


        binding.fabCreateHabit.setOnClickListener {
            (activity as MainActivity).openCreateFragment()
        }
    }


    //rvItemOnClick
    private val clickListener = object : OnRecyclerItemClicked {
        override fun onRVItemClicked(habit: Habit) {
            doOnRVItemClicked(habit)
        }
    }

    private fun doOnRVItemClicked(habit: Habit) {
        (activity as MainActivity).openCreateFragmentWithData(habit)
    }

    private fun initListTEST() {
        HabitList.createHabit(
            Habit(
                name = "Выучить Kotlin",
                description = "Выучить Kotlin по гайдам на ютубе",
                type = HabitType.GOOD,
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
                type = HabitType.BAD,
                color = 320f,
                priority = 3,
                executionQuantity = 3,
                frequency = 5
            )
        )
    }
}