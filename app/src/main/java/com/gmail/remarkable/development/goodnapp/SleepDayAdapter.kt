package com.gmail.remarkable.development.goodnapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmail.remarkable.development.goodnapp.databinding.ListItemDayBinding

class SleepDayAdapter(val clickListener: SleepDayListener) :
    ListAdapter<SleepDay, SleepDayAdapter.ViewHolder>(SleepDayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }


    class ViewHolder private constructor(val binding: ListItemDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepDay, clickListener: SleepDayListener) {
            binding.sleepDay = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDayBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SleepDayDiffCallback : DiffUtil.ItemCallback<SleepDay>() {
    override fun areItemsTheSame(oldItem: SleepDay, newItem: SleepDay): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: SleepDay, newItem: SleepDay): Boolean {
        return oldItem.date == newItem.date &&
                oldItem.targetTWT == newItem.targetTWT &&
                oldItem.wakeUp == newItem.wakeUp &&
                oldItem.outOfBed == newItem.outOfBed &&
                oldItem.realBedtime == newItem.realBedtime &&
                oldItem.naps == newItem.naps
    }

}

class SleepDayListener(val clickListener: (date: Long) -> Unit) {
    fun onClick(date: Long) = clickListener(date)
}