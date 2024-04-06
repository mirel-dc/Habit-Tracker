package com.example.habittracker.fragments

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.habittracker.R
import com.example.habittracker.data.models.Habit
import com.example.habittracker.data.models.HabitType
import com.example.habittracker.databinding.FragmentCreateHabitBinding
import com.example.habittracker.domain.HabitList
import com.example.habittracker.viewmodels.CreateHabitViewModel

private const val TAG = "CreateHabitFragment"

class CreateHabitFragment : Fragment() {

    private var _binding: FragmentCreateHabitBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentCreateHabit must not be null")


    private val viewModel: CreateHabitViewModel by viewModels()
    private val args: CreateHabitFragmentArgs by navArgs()
    private var hueColor = 0f

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

        viewModel.currentHabit = args.habit

        createColorBlock()
        initPriorityAdapter()
        initCurrentHabit()

        viewModel.nameError.observe(viewLifecycleOwner) { errorMessage ->
            binding.containerName.helperText =
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
        viewModel.initValidationErrors()

        submitBtnOnClickListener()
        habitNameFocusListener()
        habitQuantityFocusListener()
        habitFrequencyFocusListener()
    }


    //init fields with RV item's data
    private fun initCurrentHabit() = with(binding) {
        etName.setText(viewModel.currentHabit?.name)
        etDescription.setText(viewModel.currentHabit?.description)
        setRadioGroup(viewModel.currentHabit?.type ?: HabitType.GOOD)
        viewModel.currentHabit?.let { spPriority.setSelection(it.priority - 1) }
        initTVColor(viewModel.currentHabit?.color ?: 0f)

        if (viewModel.currentHabit != null) {
            etExecutionQuantity.setText(viewModel.currentHabit?.executionQuantity.toString())
            etFrequency.setText(viewModel.currentHabit?.frequency.toString())
        }
    }

    private fun initPriorityAdapter() {
        val prioritiesArrayAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                viewModel.priorities
            )
        binding.spPriority.adapter = prioritiesArrayAdapter
    }

    private fun setRadioGroup(habitType: HabitType) = with(binding) {
        when (habitType) {
            HabitType.GOOD -> rbGood.isChecked = true
            HabitType.BAD -> rbBad.isChecked = true
        }
    }

    private fun getHabitFromFields(): Habit = with(binding) {
        return Habit(
            name = etName.text.toString(),
            description = etDescription.text.toString(),
            type = getHabitType(),
            color = hueColor,
            priority = spPriority.selectedItem.toString().toInt(),
            executionQuantity = etExecutionQuantity.text.toString().toInt(),
            frequency = etFrequency.text.toString().toInt()
        )
    }

    private fun getHabitType(): HabitType = with(binding) {
        return when (true) {
            rbGood.isChecked -> HabitType.GOOD
            rbBad.isChecked -> HabitType.BAD
            else -> {
                HabitType.GOOD
            }
        }
    }

    //Submitting Habit group
    private fun submitBtnOnClickListener() {
        binding.btnSubmit.setOnClickListener {
            if (isValid()) {
                if (viewModel.currentHabit != null) {
                    updateCurrentHabit()
                    viewModel.updateHabit()
                } else {
                    viewModel.currentHabit = getHabitFromFields()
                    viewModel.createHabit()
                }
                findNavController().popBackStack()
            }
            Log.d(TAG, HabitList.getHabits().toString())
        }
    }

    private fun updateCurrentHabit() = with(binding) {
        viewModel.currentHabit?.name = etName.text.toString()
        viewModel.currentHabit?.description = etDescription.text.toString()
        viewModel.currentHabit?.type = getHabitType()
        viewModel.currentHabit?.color = hueColor
        viewModel.currentHabit?.frequency = etFrequency.text.toString().toInt()
        viewModel.currentHabit?.executionQuantity = etExecutionQuantity.text.toString().toInt()
        viewModel.currentHabit?.priority = spPriority.selectedItem.toString().toInt()
    }

    private fun isValid(): Boolean {
        return if (viewModel.validateQuantity(binding.etExecutionQuantity.text.toString())
            && viewModel.validateName(binding.etName.text.toString())
            && viewModel.validateFrequency(binding.etFrequency.text.toString())
        ) true
        else {
            Toast.makeText(requireContext(), getString(R.string.invalid_form), Toast.LENGTH_SHORT)
                .show()
            false
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
                setOnClickListener { setColor(squareSide, squareMargin, squareQuantity, i) }
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

    private fun setColor(squareSide: Int, squareMargin: Int, squareQuantity: Int, i: Int) {
        val squareLength: Float = 2f * squareMargin + squareSide
        val middlePoint: Float =
            (squareLength * i - (squareSide / 2 + squareMargin)) / (squareLength * squareQuantity) * 360

        initTVColor(middlePoint)
    }

    private fun initTVColor(hue: Float) {
        hueColor = hue
        binding.tvColor.setBackgroundColor(Color.HSVToColor(floatArrayOf(hue, 1f, 1f)))
        val rgbColor = Color.HSVToColor(floatArrayOf(hue, 1f, 1f))
        binding.tvColor.text =
            getString(
                R.string.current_color_hsv_rgb,
                hueColor,
                Color.red(rgbColor),
                Color.green(rgbColor),
                Color.blue(rgbColor)
            )
    }
}

