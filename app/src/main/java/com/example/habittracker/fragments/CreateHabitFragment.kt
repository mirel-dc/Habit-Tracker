package com.example.habittracker.fragments

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.habittracker.CallbackListener
import com.example.habittracker.R
import com.example.habittracker.data.models.Habit
import com.example.habittracker.data.models.HabitType
import com.example.habittracker.databinding.FragmentCreateHabitBinding
import com.example.habittracker.domain.HabitList

private const val TAG = "CreateHabitFragment"

class CreateHabitFragment : Fragment() {

    private val args: CreateHabitFragmentArgs by navArgs()

    private var callBack: CallbackListener? = null

    private var _binding: FragmentCreateHabitBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentCreateHabitBinding must not be null")

    private val priorities = arrayOf(1, 2, 3, 4, 5)

    private var hueColor = 0f

    private lateinit var changeHabit: Habit

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBack = activity as CallbackListener
    }

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

        val prioritiesArrayAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                priorities
            )
        binding.spPriority.adapter = prioritiesArrayAdapter

        if (isChange()) {
            changeHabit = args.habit!!
            initTVColor(changeHabit.color)
        } else initTVColor(0f)
        createColorBlock()

        setHabit()

        val habitName = binding.etName.text.toString() //Store value for diffUtils (name is a key)
        submitOnClickListener(habitName)


        habitNameFocusListener()
        habitQuantityFocusListener()
        habitFrequencyFocusListener()
    }

    //Todo  Куда выносить подобные функции?
    private inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }


    //init group
    private fun isChange(): Boolean {
        return (args.habit != null)
    }

    private fun setHabit() = with(binding) {
        if (isChange()) {
            etName.setText(changeHabit.name)
            etDescription.setText(changeHabit.description)
            setRadioGroup(getString(changeHabit.type.resId))
            spPriority.setSelection(changeHabit.priority - 1)
            etExecutionQuantity.setText(changeHabit.executionQuantity.toString())
            etFrequency.setText(changeHabit.frequency.toString())
        } else {
            rbGood.isChecked = true
            containerName.helperText = getString(R.string.required)
            containerFrequency.helperText = getString(R.string.required)
            containerExecutionQuantity.helperText = getString(R.string.required)
        }
    }

    private fun setRadioGroup(typeDescription: String?) = with(binding) {
        when (typeDescription) {
            getString(HabitType.GOOD.resId) -> rbGood.isChecked = true
            getString(HabitType.BAD.resId) -> rbBad.isChecked = true
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
        linearLayout.layoutParams = linearLayoutParams
        linearLayout.orientation = LinearLayout.HORIZONTAL

        binding.svColor.addView(linearLayout)

        for (i in 1..squareQuantity) {
            val button = Button(context)
            val buttonParams = LinearLayout.LayoutParams(squareSide, squareSide)
            buttonParams.setMargins(squareMargin, 15, squareMargin, 15)
            button.layoutParams = buttonParams
            button.text = "$i"
            context?.let { button.setTextColor(it.getColor(R.color.white)) }
            button.setBackgroundResource(R.drawable.border_color_square)

            button.setOnClickListener {
                setColor(squareSide, squareMargin, squareQuantity, i)
            }
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


    //DataList actions
    private fun updateHabit(wasHabitName: String) = with(binding) {
        HabitList.updateHabit(
            wasHabitName, Habit(
                name = etName.text.toString(),
                description = etDescription.text.toString(),
                type = getHabitType(),
                color = hueColor,
                priority = spPriority.selectedItem.toString().toInt(),
                executionQuantity = etExecutionQuantity.text.toString().toInt(),
                frequency = etFrequency.text.toString().toInt()
            )
        )
    }

    private fun createHabit() = with(binding) {
        HabitList.createHabit(
            Habit(
                name = etName.text.toString(),
                description = etDescription.text.toString(),
                type = getHabitType(),
                color = hueColor,
                priority = spPriority.selectedItem.toString().toInt(),
                executionQuantity = etExecutionQuantity.text.toString().toInt(),
                frequency = etFrequency.text.toString().toInt()
            )
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


    //Validate group
    private fun submitOnClickListener(habitName: String) {
        binding.btnSubmit.setOnClickListener {
            if (submitForm()) {
                if (isChange()) {
                    updateHabit(habitName)
                } else {
                    createHabit()
                }
                Log.d(TAG, HabitList.getHabits().toString())
                findNavController().popBackStack()
                callBack?.onCallback()
            }
        }
    }

    private fun submitForm(): Boolean {
        val validName = binding.etName.text?.isNotEmpty() == true
        val validFrequency = binding.etExecutionQuantity.text?.isNotEmpty() == true
        val validQuantity = if (binding.etFrequency.text?.isNotEmpty() == true)
            (binding.etFrequency.text.toString().toInt() <= 7) else false

        return if (validName && validFrequency && validQuantity) {
            true
        } else {
            invalidForm()
            false
        }
    }

    private fun invalidForm() {
        var message = ""
        if (binding.etName.text?.isNotEmpty() == false)
            message += getString(R.string.nameRequire) + getString(R.string.cannot_be_empty)
        if (binding.etExecutionQuantity.text?.isNotEmpty() == false)
            message += getString(R.string.execution_quantityRequired) + getString(R.string.cannot_be_empty)
        if (binding.etFrequency.text?.isNotEmpty() == false)
            message += getString(R.string.frequencyRequired) + getString(R.string.cannot_be_empty)
        else if (binding.etFrequency.text.toString().toInt() > 7)
            message += getString(R.string.frequencyRequired) + getString(R.string.cannot_be_more_then_7)

        AlertDialog.Builder(context)
            .setTitle(getString(R.string.invalid_form))
            .setMessage(message)
            .setPositiveButton(getString(R.string.okay)) { _, _ -> }.show()
    }

    private fun habitFrequencyFocusListener() {
        binding.etFrequency.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.containerFrequency.helperText = validFrequency()
            }
        }
    }

    private fun validFrequency(): String? {
        val frequency = binding.etFrequency.text.toString()
        if (frequency.isEmpty()) {
            return getString(R.string.cannot_be_empty)
        }
        if (frequency.toInt() > 7) {
            return getString(R.string.cannot_be_more_then_7)
        }
        return null
    }

    private fun habitQuantityFocusListener() {
        binding.etExecutionQuantity.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.containerExecutionQuantity.helperText = validQuantity()
            }
        }
    }

    private fun validQuantity(): String? {
        val quantity = binding.etExecutionQuantity.text.toString()
        if (quantity.isEmpty()) {
            return getString(R.string.cannot_be_empty)
        }
        return null
    }

    private fun habitNameFocusListener() {
        binding.etName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.containerName.helperText = validName()
            }
        }
    }

    private fun validName(): String? {
        val nameText = binding.etName.text.toString()
        if (nameText.isEmpty()) {
            return getString(R.string.cannot_be_empty)
        }
        return null
    }
}

