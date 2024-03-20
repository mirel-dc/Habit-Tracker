package com.example.habittracker.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.data.models.Habit
import com.example.habittracker.databinding.ItemHabitatDataBinding

class HabitAdapter(
    context: Context,
    private val clickListener: OnRecyclerItemClicked,
) : RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    private var habits = mutableListOf<Habit>()

    fun setData(data: MutableList<Habit>) {
        this.habits = data
    }

    fun setNewData(newData: MutableList<Habit>) {
        val diffCallback = HabitCallback(habits, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        habits.clear()
        habits.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_habitat_data, parent, false))
    }

    override fun getItemCount(): Int = habits.size

    private fun getItem(position: Int): Habit = habits[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickListener.onRVItemClicked(habits[position])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemHabitatDataBinding.bind(view)

        fun bind(habit: Habit) = with(binding) {
            tvHabitName.text = habit.name
            tvHabitDescription.text = habit.description
            tvHabitType.text = context.getString(habit.type.resId)
            tvHabitFrequency.text = context.getString(R.string.week, habit.frequency.toString())
            viewColor.setBackgroundColor(Color.HSVToColor(floatArrayOf(habit.color, 1f, 1f)))
            tvHabitPriority.text = habit.priority.toString()
        }
    }


}

interface OnRecyclerItemClicked {
    fun onRVItemClicked(habit: Habit)
}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context