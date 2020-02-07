package com.gmail.remarkable.development.goodnapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmail.remarkable.development.goodnapp.databinding.ListItemDayBinding
import com.gmail.remarkable.development.goodnapp.util.getDateString
import com.gmail.remarkable.development.goodnapp.util.getStringForRealTWT

class SleepDayAdapter : ListAdapter<SleepDay, SleepDayAdapter.ViewHolder>(SleepDayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class ViewHolder private constructor(val binding: ListItemDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepDay) {
            val holderContext = itemView.context
            val res = holderContext.resources
            binding.itemListDate.text = getDateString(item.date, holderContext)
            binding.itemListTWT.text = getStringForRealTWT(item.realTWT, res)
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
        return oldItem == newItem
    }

}