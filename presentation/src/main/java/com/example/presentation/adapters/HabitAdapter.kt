package com.example.presentation.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.data.local.entity.HabitEntity
import com.example.presentation.R
import com.example.presentation.databinding.ItemHabitatDataBinding
import com.example.presentation.utils.GetResIdFromEnum

class HabitAdapter(
    context: Context,
    private val clickListener: OnRecyclerItemClicked,
    private val btnCompleteClick: OnBtnCompleteClickListener
) : ListAdapter<HabitEntity, HabitAdapter.ViewHolder>(HabitDiffItemCallback()) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            inflater.inflate(
                R.layout.item_habitat_data, parent, false,
            ), btnCompleteClick = btnCompleteClick
        )
    }

    override fun getItemCount(): Int = currentList.size

    override fun getItem(position: Int): HabitEntity = currentList[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickListener.onRVItemClicked(currentList[position])
        }
    }

    class ViewHolder(
        view: View,
        private val btnCompleteClick: OnBtnCompleteClickListener
    ) : RecyclerView.ViewHolder(view) {
        private val binding = ItemHabitatDataBinding.bind(view)

        fun bind(habitEntity: HabitEntity) = with(binding) {
            tvHabitName.text = habitEntity.name
            tvHabitDescription.text = habitEntity.description
            tvHabitType.text =
                context.getString(GetResIdFromEnum.fromType(habitEntity.type))
            tvHabitFrequency.text =
                context.getString(R.string.week, habitEntity.frequency.toString())
            viewColor.setBackgroundColor(Color.HSVToColor(floatArrayOf(habitEntity.color, 1f, 1f)))
            tvHabitPriority.text =
                context.getString(GetResIdFromEnum.fromPriority(habitEntity.priority))
            //Todo Click listener
            btnComplete.setOnClickListener {
                btnCompleteClick.onBtnCompleteClicked(habitEntity)
            }
        }
    }
}

interface OnRecyclerItemClicked {
    fun onRVItemClicked(habitEntity: HabitEntity)
}

interface OnBtnCompleteClickListener {
    fun onBtnCompleteClicked(habitEntity: HabitEntity)
}


private val RecyclerView.ViewHolder.context
    get() = this.itemView.context