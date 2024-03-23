package com.example.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.habittracker.adapters.TypeFragmentAdapter
import com.example.habittracker.data.models.HabitType
import com.example.habittracker.databinding.FragmentMainHolderBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainHolderFragment : Fragment() {

    private var _binding: FragmentMainHolderBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentMainHolderBinding must not be null")

    private lateinit var adapter: TypeFragmentAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainHolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TypeFragmentAdapter(requireActivity())
        viewPager = binding.habitTypeViewPager2
        viewPager.adapter = adapter
        TabLayoutMediator(
            binding.habitTypeTabLayout,
            binding.habitTypeViewPager2
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(HabitType.GOOD.resId)
                1 -> tab.text = getString(HabitType.BAD.resId)
            }
        }.attach()


        binding.fabCreateHabit.setOnClickListener {
            val navAction = MainHolderFragmentDirections.actionMainHolderFragmentToCreateHabitFragment(null)
            findNavController().navigate(navAction)
        }
    }
}