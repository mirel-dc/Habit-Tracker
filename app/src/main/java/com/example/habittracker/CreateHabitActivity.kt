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
        createColorBlock(200, 50, 32)

        val habitName = binding.etName.text.toString() //Store value for diffUtils (name is a key)
        submitOnClickListener(habitName)

        setTVColor(intent.getFloatExtra(COLOR, 0f))

        habitNameFocusListener()
        habitQuantityFocusListener()
        habitFrequencyFocusListener()
    }

    private fun createColorBlock(squareSide: Int, squareMargin: Int, squareQuantity: Int) {
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

    private fun submitForm(): Boolean {
        //ClearFocus for all helperTexts to set
        binding.etName.clearFocus()
        binding.etExecutionQuantity.clearFocus()
        binding.etFrequency.clearFocus()

        val validName = binding.containerName.helperText == null
        val validFrequency = binding.containerFrequency.helperText == null
        val validQuantity = binding.containerExecutionQuantity.helperText == null

        return if (validName && validFrequency && validQuantity) {
            true
        } else {
            invalidForm()
            false
        }
    }

    private fun invalidForm() {
        var message = ""
        if (binding.containerName.helperText != null)
            message += getString(R.string.nameRequire) + binding.containerName.helperText
        if (binding.containerFrequency.helperText != null)
            message += getString(R.string.frequencyRequired) + binding.containerFrequency.helperText
        if (binding.containerExecutionQuantity.helperText != null)
            message += getString(R.string.execution_quantityRequired) + binding.containerExecutionQuantity.helperText

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.invalid_form))
            .setMessage(message)
            .setPositiveButton(getString(R.string.okay)) { _, _ -> }.show()
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

    private fun updateHabit(wasHabitName: String) = with(binding) {
        HabitList().updateHabit(
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
        HabitList().createHabit(
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

    private fun isChange(): Boolean {
        return intent.getBooleanExtra("change", false)
    }

    private fun setRadioGroup(typeDescription: String?) = with(binding) {
        when (typeDescription) {
            HabitAdapter.HabitType.LEARN.description -> rbLearn.isChecked = true
            HabitAdapter.HabitType.SPORT.description -> rbSport.isChecked = true
            HabitAdapter.HabitType.HEALTH.description -> rbHealth.isChecked = true
            HabitAdapter.HabitType.SOCIAL.description -> rbSocial.isChecked = true
        }
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
