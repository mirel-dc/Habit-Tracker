package com.example.habittracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.adapters.HabitAdapter
import com.example.habittracker.adapters.OnRecyclerItemClicked
import com.example.habittracker.data.models.Habit
import com.example.habittracker.data.models.HabitType
import com.example.habittracker.databinding.FragmentHabitsListBinding
import com.example.habittracker.domain.HabitList
import com.example.habittracker.utils.SpacingItemDecorator
import com.example.habittracker.utils.parcelable
import com.example.habittracker.viewmodels.HabitListViewModel

private const val TAG = "HabitList"

class HabitsListFragment : Fragment() {

    private val viewModel: HabitListViewModel by viewModels()

    private var _binding: FragmentHabitsListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentHabitsListBinding must not be null")

    private var _adapter: HabitAdapter? = null
    private val adapter
        get() = _adapter ?: throw IllegalStateException("Adapter must not be null")

    private lateinit var habitType: HabitType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsListBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Habit type for filter
        arguments?.takeIf { it.containsKey(PARAM_TYPE) }?.apply {
            habitType = parcelable(PARAM_TYPE)!!
            Log.d(TAG, habitType.toString())
        }
        setupRecyclerView()

        viewModel.habitsLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "Trying set new data - ${viewModel.habitsByType(habitType)}")
            adapter.setNewData(viewModel.habitsByType(habitType))
        }
    }

    private fun setupRecyclerView() {
        _adapter = context?.let {
            HabitAdapter(it, clickListener)
        }

        adapter.setData(viewModel.habitsByType(habitType))
        binding.rvHabit.adapter = adapter
        binding.rvHabit.addItemDecoration(SpacingItemDecorator(16))
        binding.rvHabit.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }


    //rvItemOnClick
    private val clickListener = object : OnRecyclerItemClicked {
        override fun onRVItemClicked(habit: Habit) {
            doOnRVItemClicked(habit)
        }
    }

    private fun doOnRVItemClicked(habit: Habit) {
        Log.d(TAG, HabitList.getHabits().toString())
        val navAction =
            MainHolderFragmentDirections.actionMainHolderFragmentToCreateHabitFragment(habit)
        findNavController().navigate(navAction)
    }

    companion object {
        private const val PARAM_TYPE = "param_type"

        fun newInstance(habitType: HabitType): HabitsListFragment {
            val fragment = HabitsListFragment()
            val args = Bundle()
            args.putParcelable(PARAM_TYPE, habitType)
            fragment.arguments = args
            return fragment
        }
    }
}
