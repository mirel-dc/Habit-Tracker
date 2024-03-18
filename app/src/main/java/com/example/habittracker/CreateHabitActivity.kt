package com.example.habittracker

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.habittracker.adapters.HabitAdapter
import com.example.habittracker.data.models.Habit
import com.example.habittracker.databinding.ActivityCreateHabitBinding
import com.example.habittracker.domain.HabitList

class CreateHabitActivity : AppCompatActivity() {
    private var _binding: ActivityCreateHabitBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityCreateHabitBinding must not be null")

    private val priorities = arrayOf(1, 2, 3, 4, 5)

    private var hueColor = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prioritiesArrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        binding.spPriority.adapter = prioritiesArrayAdapter

        setHabit()
        createColorBlock()

        val habitName = binding.etName.text.toString() //Store value for diffUtils (name is a key)
        submitOnClickListener(habitName)

        setTVColor(intent.getFloatExtra(COLOR, 0f))

        habitNameFocusListener()
        habitQuantityFocusListener()
        habitFrequencyFocusListener()
    }


    //init group
    private fun isChange(): Boolean {
        return intent.getBooleanExtra(CHANGE, false)
    }

    private fun setHabit() = with(binding) {
        if (isChange()) {
            etName.setText(intent.getStringExtra(NAME))
            etDescription.setText(intent.getStringExtra(DESCRIPTION))
            setRadioGroup(intent.getStringExtra(TYPE))
            spPriority.setSelection(intent.getIntExtra(PRIORITY, 1) - 1)
            etExecutionQuantity.setText(
                intent.getIntExtra(EXECUTIONQUANTITY, 1).toString()
            )
            etFrequency.setText(intent.getIntExtra(FREQUENCY, 1).toString())
        } else {
            containerName.helperText = getString(R.string.required)
            containerFrequency.helperText = getString(R.string.required)
            containerExecutionQuantity.helperText = getString(R.string.required)
        }
    }

    private fun setRadioGroup(typeDescription: String?) = with(binding) {
        when (typeDescription) {
            getString(HabitAdapter.HabitType.LEARN.resId) -> rbLearn.isChecked = true
            getString(HabitAdapter.HabitType.SPORT.resId) -> rbSport.isChecked = true
            getString(HabitAdapter.HabitType.HEALTH.resId) -> rbHealth.isChecked = true
            getString(HabitAdapter.HabitType.SOCIAL.resId) -> rbSocial.isChecked = true
        }
    }


    //ColorPicker view group
    private fun createColorBlock() {
        val squareSide = 200
        val squareMargin = 50
        val squareQuantity = 16

        val linearLayout = LinearLayout(this)
        val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.layoutParams = linearLayoutParams
        linearLayout.orientation = LinearLayout.HORIZONTAL

        binding.svColor.addView(linearLayout)

        for (i in 1..squareQuantity) {
            val button = Button(this)
            val buttonParams = LinearLayout.LayoutParams(squareSide, squareSide)
            buttonParams.setMargins(squareMargin, 15, squareMargin, 15)
            button.layoutParams = buttonParams
            button.text = "$i"
            button.setTextColor(getColor(R.color.white))
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

        setTVColor(middlePoint)
    }

    private fun setTVColor(hue: Float) {
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

    //RecyclerView on item click listener
    private fun submitOnClickListener(habitName: String) {
        binding.btnSubmit.setOnClickListener {
            if (submitForm()) {
                if (isChange()) {
                    updateHabit(habitName)
                } else {
                    createHabit()
                }
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
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
                type = HabitAdapter.HabitType.LEARN,
                color = hueColor,
                priority = spPriority.selectedItem.toString().toInt(),
                executionQuantity = etExecutionQuantity.text.toString().toInt(),
                frequency = etFrequency.text.toString().toInt()
            )
        )
    }


    private fun getHabitType(): HabitAdapter.HabitType = with(binding) {
        return when (true) {
            rbLearn.isChecked -> HabitAdapter.HabitType.LEARN
            rbHealth.isChecked -> HabitAdapter.HabitType.HEALTH
            rbSocial.isChecked -> HabitAdapter.HabitType.SOCIAL
            rbSport.isChecked -> HabitAdapter.HabitType.SPORT
            else -> {
                HabitAdapter.HabitType.LEARN
            }
        }
    }


    //Validate group
    private fun submitForm(): Boolean {
        val validName = binding.etName.text?.isNotEmpty() == true
        val validFrequency = binding.etFrequency.text?.isNotEmpty() == true
        val validQuantity = if (binding.etExecutionQuantity.text?.isNotEmpty() == true)
            (binding.etExecutionQuantity.text.toString().toInt() <= 7) else false

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

        AlertDialog.Builder(this)
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
