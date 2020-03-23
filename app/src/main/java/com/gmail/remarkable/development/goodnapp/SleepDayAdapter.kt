package com.gmail.remarkable.development.goodnapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmail.remarkable.development.goodnapp.databinding.ListItemDayTimelineBinding

class SleepDayAdapter(val clickListener: SleepDayListener) :
    ListAdapter<Pair<SleepDay, Long?>, SleepDayAdapter.ViewHolder>(SleepDayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }


    class ViewHolder private constructor(val binding: ListItemDayTimelineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<SleepDay, Long?>, clickListener: SleepDayListener) {
            binding.sleepDay = item.first
            binding.nextDayWakeUp = item.second
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDayTimelineBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SleepDayDiffCallback : DiffUtil.ItemCallback<Pair<SleepDay, Long?>>() {
    override fun areItemsTheSame(
        oldItem: Pair<SleepDay, Long?>,
        newItem: Pair<SleepDay, Long?>
    ): Boolean {
        return oldItem.first.date == newItem.first.date
    }

    override fun areContentsTheSame(
        oldItem: Pair<SleepDay, Long?>,
        newItem: Pair<SleepDay, Long?>
    ): Boolean {
        return oldItem.first.date == newItem.first.date &&
                oldItem.first.targetTWT == newItem.first.targetTWT &&
                oldItem.first.wakeUp == newItem.first.wakeUp &&
                oldItem.first.outOfBed == newItem.first.outOfBed &&
                oldItem.first.realBedtime == newItem.first.realBedtime &&
                oldItem.first.naps == newItem.first.naps &&
                oldItem.second == newItem.second
    }

}

class SleepDayListener(val clickListener: (date: Long) -> Unit) {
    fun onClick(date: Long) = clickListener(date)
}