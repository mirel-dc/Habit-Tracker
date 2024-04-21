package com.example.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.adapters.HabitAdapter
import com.example.habittracker.adapters.OnRecyclerItemClicked
import com.example.habittracker.data.models.Habit
import com.example.habittracker.data.models.HabitType
import com.example.habittracker.databinding.FragmentHabitsListBinding
import com.example.habittracker.db.HabitDB
import com.example.habittracker.factory.HabitListViewModelFactory
import com.example.habittracker.repository.HabitRepository
import com.example.habittracker.utils.SpacingItemDecorator
import com.example.habittracker.utils.parcelable
import com.example.habittracker.viewmodels.HabitListViewModel
import com.google.android.material.snackbar.Snackbar

private const val TAG = "HabitList"

class HabitsListFragment : Fragment() {

    private var _binding: FragmentHabitsListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentHabitsList must not be null")

    private var _adapter: HabitAdapter? = null
    private val adapter
        get() = _adapter ?: throw IllegalStateException("Adapter must not be null")

    private val viewModel: HabitListViewModel by activityViewModels {
        HabitListViewModelFactory(HabitRepository(HabitDB.getHabitDB(requireContext())))
    }
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

        //Habit type for viewPager
        arguments?.takeIf { it.containsKey(PARAM_TYPE) }?.apply {
            habitType = parcelable(PARAM_TYPE)!!
        }
        initRecyclerView()

        HabitDB.getHabitDB(requireContext()).getDao().getAllHabits().observe(viewLifecycleOwner) {
            viewModel.updateLiveData()
        }

        viewModel.habitsLiveData.observe(viewLifecycleOwner) { newList ->
            viewModel.setCurrentList(newList)
            adapter.submitList(viewModel.getHabitsByType(habitType))
        }

        viewModel.filterByLiveData.observe(requireActivity()) {
            adapter.submitList(viewModel.getHabitsByType(habitType))
        }

        viewModel.searchNameLiveData.observe(requireActivity()) {
            adapter.submitList(viewModel.getHabitsByType(habitType))
        }
    }

    private fun initRecyclerView() {
        _adapter = context?.let {
            HabitAdapter(it, clickListener)
        }

        binding.rvHabit.adapter = adapter
        binding.rvHabit.addItemDecoration(SpacingItemDecorator(16))
        binding.rvHabit.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.START
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val habit = adapter.currentList[position]
                viewModel.deleteHabit(habit)

                Snackbar.make(
                    requireView(),
                    getString(R.string.successfully_deleted_habit),
                    Snackbar.LENGTH_LONG
                ).apply {
                    setAction(getString(R.string.undo)) {
                        viewModel.createHabit(habit)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvHabit)
        }
    }


    //rvItemOnClick
    private val clickListener = object : OnRecyclerItemClicked {
        override fun onRVItemClicked(habit: Habit) {
            doOnRVItemClicked(habit)
        }
    }

    private fun doOnRVItemClicked(habit: Habit) {
        val navAction =
            MainHolderFragmentDirections.actionMainHolderFragmentToCreateHabitFragment(habit.id.toString())
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

