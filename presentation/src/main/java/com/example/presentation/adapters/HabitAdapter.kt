package com.example.presentation.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.data.local.entity.HabitEntity
import com.example.data.local.entity.HabitPriority
import com.example.data.local.entity.HabitType
import com.example.habittracker.databinding.ItemHabitatDataBinding

class HabitAdapter(
    context: Context,
    private val clickListener: OnRecyclerItemClicked,
) : ListAdapter<HabitEntity, HabitAdapter.ViewHolder>(HabitDiffItemCallback()) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_habitat_data, parent, false))
    }

    override fun getItemCount(): Int = currentList.size

    override fun getItem(position: Int): HabitEntity = currentList[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickListener.onRVItemClicked(currentList[position])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemHabitatDataBinding.bind(view)

        fun bind(habitEntity: HabitEntity) = with(binding) {
            tvHabitName.text = habitEntity.name
            tvHabitDescription.text = habitEntity.description
            tvHabitType.text =
                context.getString(HabitType.getResourceIdByType(habitEntity.type))
            tvHabitFrequency.text =
                context.getString(R.string.week, habitEntity.frequency.toString())
            viewColor.setBackgroundColor(Color.HSVToColor(floatArrayOf(habitEntity.color, 1f, 1f)))
            tvHabitPriority.text =
                context.getString(HabitPriority.getResourceIdByPriority(habitEntity.priority))
        }
    }
}

interface OnRecyclerItemClicked {
    fun onRVItemClicked(habitEntity: HabitEntity)
}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context