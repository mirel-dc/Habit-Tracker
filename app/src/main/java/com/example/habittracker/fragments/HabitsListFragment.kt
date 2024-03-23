package com.example.habittracker.fragments

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.CallbackListener
import com.example.habittracker.MainActivity
import com.example.habittracker.adapters.HabitAdapter
import com.example.habittracker.adapters.OnRecyclerItemClicked
import com.example.habittracker.data.models.Habit
import com.example.habittracker.data.models.HabitType
import com.example.habittracker.databinding.FragmentHabitsListBinding
import com.example.habittracker.domain.HabitList
import com.example.habittracker.utils.SpacingItemDecorator

private const val TAG = "HabitList"

class HabitsListFragment : Fragment(), CallbackListener {
    private lateinit var call: MainActivity

    private var _binding: FragmentHabitsListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentHabitsListBinding must not be null")

    private var _adapter: HabitAdapter? = null
    private val adapter
        get() = _adapter ?: throw IllegalStateException("Adapter must not be null")

    private var habitType = HabitType.GOOD

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsListBinding.inflate(inflater, container, false)

        call = context as MainActivity
        call.setCallback(this)

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
        }


        val recycler = binding.rvHabit
        _adapter = context?.let {
            HabitAdapter(it, clickListener)
        }
        adapter.setData(HabitList.getHabitByType(habitType))
        recycler.adapter = adapter
        recycler.addItemDecoration(SpacingItemDecorator(16))
        recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }

    //Callback for update RV
    override fun onCallback() {
        Log.d(TAG, "OnCallback")
        Log.d(TAG, HabitList.getHabitByType(habitType).toString())
        adapter.setNewData(HabitList.getHabitByType(habitType))
    }

    //rvItemOnClick
    private val clickListener = object : OnRecyclerItemClicked {
        override fun onRVItemClicked(habit: Habit) {
            doOnRVItemClicked(habit)
        }
    }

    private fun doOnRVItemClicked(habit: Habit) {
        Log.d(TAG,HabitList.getHabits().toString())
        val navAction = MainHolderFragmentDirections.actionMainHolderFragmentToCreateHabitFragment(habit)
        findNavController().navigate(navAction)

        adapter.setNewData(HabitList.getHabitByType(habitType))
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
