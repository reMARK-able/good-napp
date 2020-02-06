package com.gmail.remarkable.development.goodnapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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


    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.item_list_date)
        val twt: TextView = itemView.findViewById(R.id.item_list_TWT)

        fun bind(item: SleepDay) {
            val holderContext = itemView.context
            val res = holderContext.resources
            date.text = getDateString(item.date, holderContext)
            twt.text = getStringForRealTWT(item.realTWT, res)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view =
                    layoutInflater.inflate(R.layout.list_item_day, parent, false)
                return ViewHolder(view)
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