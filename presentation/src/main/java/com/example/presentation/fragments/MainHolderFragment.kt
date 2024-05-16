package com.example.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.domain.model.HabitType
import com.example.habittracker.databinding.FragmentMainHolderBinding
import com.example.presentation.adapters.TypeFragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainHolderFragment : Fragment() {

    private var _binding: FragmentMainHolderBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentMainHolder must not be null")

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

        adapter = TypeFragmentAdapter(this)
        viewPager = binding.habitTypeViewPager2
        viewPager.adapter = adapter
        TabLayoutMediator(
            binding.habitTypeTabLayout,
            binding.habitTypeViewPager2
        ) { tab, position ->
            when (position) {
                0 -> tab.text =
                    getString(
                        HabitType.getResourceIdByType(
                            HabitType.getHabitTypeByValue(position)
                        )
                    )

                1 -> tab.text = getString(
                    HabitType.getResourceIdByType(
                        HabitType.getHabitTypeByValue(position)
                    )
                )
            }
        }.attach()

        //Replacing container to save the anchor settings of the fab
        val bottomSheetFilter = BottomSheetFilterFragment()
        childFragmentManager.beginTransaction()
            .replace(binding.containerBottomSheet.id, bottomSheetFilter)
            .commit()

        binding.fabCreateHabit.setOnClickListener {
            val navAction =
                MainHolderFragmentDirections.actionMainHolderFragmentToCreateHabitFragment(
                    null
                )
            findNavController().navigate(navAction)
        }
    }
}