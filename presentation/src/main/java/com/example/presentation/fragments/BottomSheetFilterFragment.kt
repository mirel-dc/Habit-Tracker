package com.example.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.presentation.R
import com.example.presentation.databinding.BottomSheetBinding
import com.example.presentation.di.PresentationComponentProvider
import com.example.presentation.viewmodels.HabitListViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class BottomSheetFilterFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for BottomSheetFilterFragment must not be null")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: HabitListViewModel


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
        bottomSheetBehavior.apply {
            peekHeight = resources.getDimension(R.dimen.BottomSheetPeekValue).toInt()
            isDraggable = true
            isHideable = false
        }

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