package com.example.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.data.local.entity.HabitEntity
import com.example.domain.model.HabitType
import com.example.presentation.R
import com.example.presentation.adapters.HabitAdapter
import com.example.presentation.adapters.OnBtnCompleteClickListener
import com.example.presentation.adapters.OnRecyclerItemClicked
import com.example.presentation.databinding.FragmentHabitsListBinding
import com.example.presentation.di.PresentationComponentProvider
import com.example.presentation.utils.SpacingItemDecorator
import com.example.presentation.viewmodels.HabitListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "HabitListFragment"

class HabitsListFragment : Fragment() {

    private var _binding: FragmentHabitsListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentHabitsList must not be null")

    private var _adapter: HabitAdapter? = null
    private val adapter
        get() = _adapter ?: throw IllegalStateException("Adapter must not be null")

    //rvItemOnClick
    private val clickListener = object : OnRecyclerItemClicked {
        override fun onRVItemClicked(habitEntity: HabitEntity) {
            doOnRVItemClicked(habitEntity)
        }
    }

    private val completeClickListener = object : OnBtnCompleteClickListener {
        override fun onBtnCompleteClicked(habitEntity: HabitEntity) {
            doOnBtnCompleteClicked(habitEntity)
        }
    }


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: HabitListViewModel

    private lateinit var habitType: HabitType

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as PresentationComponentProvider).provideAppComponent()
            .inject(this)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(HabitListViewModel::class.java)
    }

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
            habitType = HabitType.fromValue(this.getInt(PARAM_TYPE))
        }

        //Log.d(TAG, arguments?.takeIf { it.containsKey(PARAM_TYPE) }.toString())

        initRecyclerView()
        initViewModelObservers()
    }

    private fun initViewModelObservers() {
        viewModel.habitsLiveData.observe(viewLifecycleOwner) { newList ->
            Log.d(TAG, newList.toString())
            viewModel.setCurrentList(newList)
            adapter.submitList(viewModel.getHabitsByType(habitType))
        }

        viewModel.filterByLiveData.observe(requireActivity()) {
            adapter.submitList(viewModel.getHabitsByType(habitType))
        }

        viewModel.searchNameLiveData.observe(requireActivity()) {
            adapter.submitList(viewModel.getHabitsByType(habitType))
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main) {
                    viewModel.toastFlow.collectLatest {
                        Toast.makeText(
                            requireContext(),
                            it, //resources.getString(it),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        _adapter = context?.let {
            HabitAdapter(it, clickListener, completeClickListener)
        }

        binding.rvHabit.adapter = adapter
        binding.rvHabit.addItemDecoration(SpacingItemDecorator(16))
        binding.rvHabit.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )

        //Deleting item with swipe left
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


    //Click listeners
    private fun doOnRVItemClicked(habitEntity: HabitEntity) {
        val navAction =
            MainHolderFragmentDirections.actionMainHolderFragmentToCreateHabitFragment(habitEntity.id.toString())
        findNavController().navigate(navAction)
    }


    private fun doOnBtnCompleteClicked(habitEntity: HabitEntity) {
        viewModel.btnCompleteClicked(habitEntity)
    }

    companion object {
        private const val PARAM_TYPE = "param_type"

        fun newInstance(habitType: HabitType): HabitsListFragment {
            val fragment = HabitsListFragment()
            val args = Bundle()
            args.putInt(PARAM_TYPE, habitType.value)
            fragment.arguments = args
            return fragment
        }
    }
}

