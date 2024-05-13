package com.example.habittracker.presentation.fragments

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.habittracker.R
import com.example.habittracker.data.local.db.HabitDB
import com.example.habittracker.data.local.entity.HabitEntity
import com.example.habittracker.data.local.entity.HabitPriority
import com.example.habittracker.data.local.entity.HabitType
import com.example.habittracker.data.repository.HabitRepository
import com.example.habittracker.databinding.FragmentCreateHabitBinding
import com.example.habittracker.presentation.viewmodels.CreateHabitViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "CreateHabitFragment"

class CreateHabitFragment : Fragment() {

    private var _binding: FragmentCreateHabitBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentCreateHabit must not be null")


    private val viewModel: CreateHabitViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CreateHabitViewModel(HabitRepository(HabitDB(requireContext()))) as T
            }
        }
    }
    private val args: CreateHabitFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set chosen RV item's data into View Model
        if (args.habitUUID != null) viewModel.setCurrentHabitWithUUID(args.habitUUID)
        else viewModel.emptyCurrentHabit()

        createColorBlock()
        initPriorityAdapter()

        focusListeners()
        initViewModelObservers()
        fieldsListeners()
    }

    private fun focusListeners() {
        habitNameFocusListener()
        habitQuantityFocusListener()
        habitFrequencyFocusListener()
        habitDescriptionFocusListener()
    }

    private fun initViewModelObservers() {
        viewModel.initValidationErrors()

        viewModel.nameError.observe(viewLifecycleOwner) { errorMessage ->
            binding.containerName.helperText =
                errorMessage?.let { resources.getString(it) }
        }

        viewModel.descriptionError.observe(viewLifecycleOwner) { errorMessage ->
            binding.containerDescription.helperText =
                errorMessage?.let { resources.getString(it) }
        }

        viewModel.quantityError.observe(viewLifecycleOwner) { errorMessage ->
            binding.containerExecutionQuantity.helperText =
                errorMessage?.let { resources.getString(it) }
        }

        viewModel.frequencyError.observe(viewLifecycleOwner) { errorMessage ->
            binding.containerFrequency.helperText =
                errorMessage?.let { resources.getString(it) }
        }

        viewModel.currentHabit.observe(viewLifecycleOwner) { habit ->
            initCurrentHabit(habit)
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main) {
                    viewModel.toastFlow.collectLatest {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(it),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun fieldsListeners() = with(binding) {
        etName.addTextChangedListener {
            viewModel.currentHabit.value?.name = it.toString()
        }

        etDescription.addTextChangedListener {
            viewModel.currentHabit.value?.description = it.toString()
        }

        etExecutionQuantity.addTextChangedListener {
            viewModel.validateQuantity(it.toString())
            if (it.toString() != "")
                viewModel.currentHabit.value?.executionQuantity = it.toString().toInt()
        }

        etFrequency.addTextChangedListener {
            viewModel.validateFrequency(it.toString())
            if (it.toString() != "")
                viewModel.currentHabit.value?.frequency = it.toString().toInt()
        }

        spPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.currentHabit.value?.priority =
                    HabitPriority.getHabitPriorityByValue(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.rgHabitType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbGood.id -> viewModel.currentHabit.value?.type = HabitType.GOOD
                binding.rbBad.id -> viewModel.currentHabit.value?.type = HabitType.BAD
            }
        }

        submitBtnOnClickListener()

        //Color listener in ColorBlock
    }


    //init fields with RV item's data
    private fun initCurrentHabit(habitEntity: HabitEntity?) = with(binding) {
        etName.setText(habitEntity?.name)
        etDescription.setText(habitEntity?.description)
        setRadioGroup(habitEntity?.type ?: HabitType.GOOD)
        habitEntity?.let { spPriority.setSelection(it.priority.value) }
        initTVColor(habitEntity?.color ?: 0f)
        etExecutionQuantity.setText(habitEntity?.executionQuantity.toString())
        etFrequency.setText(habitEntity?.frequency.toString())
    }

    private fun initPriorityAdapter() {
        val prioritiesArrayAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                viewModel.priorities.map { resId ->
                    requireContext().resources.getString(resId)
                }
            )
        binding.spPriority.adapter = prioritiesArrayAdapter
    }

    private fun setRadioGroup(habitType: HabitType) = with(binding) {
        when (habitType) {
            HabitType.GOOD -> rbGood.isChecked = true
            HabitType.BAD -> rbBad.isChecked = true
        }
    }


    //Submitting Habit group
    private fun submitBtnOnClickListener() {
        binding.btnSubmit.setOnClickListener {
            //Returns True when submit is valid
            if (viewModel.submitBtnAction())
                findNavController().popBackStack()
        }
    }

    //EditText Helpers
    private fun habitNameFocusListener() {
        binding.etName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validateName(binding.etName.text.toString())
            }
        }
    }

    private fun habitDescriptionFocusListener() {
        binding.etDescription.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validateDescription(binding.etDescription.text.toString())
            }
        }
    }

    private fun habitQuantityFocusListener() {
        binding.etExecutionQuantity.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validateQuantity(binding.etExecutionQuantity.text.toString())
            }
        }
    }

    private fun habitFrequencyFocusListener() {
        binding.etFrequency.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.validateFrequency(binding.etFrequency.text.toString())
            }
        }
    }


    //ColorPicker view group
    private fun createColorBlock() {
        val squareSide = 200
        val squareMargin = 50
        val squareQuantity = 16

        val linearLayout = LinearLayout(context)
        val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        linearLayout.apply {
            layoutParams = linearLayoutParams
            orientation = LinearLayout.HORIZONTAL
        }

        binding.svColor.addView(linearLayout)

        for (i in 1..squareQuantity) {
            val button = Button(context)
            val buttonParams = LinearLayout.LayoutParams(squareSide, squareSide)
            buttonParams.setMargins(squareMargin, 15, squareMargin, 15)
            button.apply {
                layoutParams = buttonParams
                text = "$i"
                setBackgroundResource(R.drawable.border_color_square)
                setOnClickListener {
                    viewModel.currentHabit.value?.color =
                        setColor(squareSide, squareMargin, squareQuantity, i)
                }
            }
            context?.let { button.setTextColor(it.getColor(R.color.white)) }
            linearLayout.addView(button)
        }

        val colors = IntArray(360)
        for (i in 0..359) {
            colors[i] = Color.HSVToColor(floatArrayOf(i.toFloat(), 1f, 1f))
        }
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
        linearLayout.background = gradientDrawable
    }

    private fun setColor(squareSide: Int, squareMargin: Int, squareQuantity: Int, i: Int): Float {
        val squareLength: Float = 2f * squareMargin + squareSide
        val middlePoint: Float =
            (squareLength * i - (squareSide / 2 + squareMargin)) / (squareLength * squareQuantity) * 360

        initTVColor(middlePoint)
        return middlePoint
    }

    private fun initTVColor(hue: Float) {
        binding.tvColor.setBackgroundColor(Color.HSVToColor(floatArrayOf(hue, 1f, 1f)))
        val rgbColor = Color.HSVToColor(floatArrayOf(hue, 1f, 1f))
        binding.tvColor.text =
            getString(
                R.string.current_color_hsv_rgb,
                hue,
                Color.red(rgbColor),
                Color.green(rgbColor),
                Color.blue(rgbColor)
            )
    }
}

