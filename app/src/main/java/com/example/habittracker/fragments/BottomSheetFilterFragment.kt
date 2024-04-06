package com.example.habittracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.example.habittracker.databinding.BottomSheetBinding
import com.example.habittracker.viewmodels.HabitListViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFilterFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for BottomSheetFilterFragment must not be null")

    private val viewModel: HabitListViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.root.parent as View)
        bottomSheetBehavior.peekHeight = 200
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.isDraggable = true


        binding.ibFilterAsc.setOnClickListener {
            Log.d(TAG, "ibFilterAsc clicked")
            viewModel.filterByAsc()
        }

        binding.ibFilterDesc.setOnClickListener {
            Log.d(TAG, "ibFilterDesc clicked")
            viewModel.filterByDesc()
        }

        binding.etFilterName.addTextChangedListener {
            viewModel.setSearchingName(it.toString())
        }

        binding.etFilterName.setText(viewModel.searchNameLiveData.value)
    }


    companion object {
        const val TAG = "BottomSheetFilterFragment"
    }
}