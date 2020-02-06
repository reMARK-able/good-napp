package com.gmail.remarkable.development.goodnapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.remarkable.development.goodnapp.util.getDateString
import com.gmail.remarkable.development.goodnapp.util.getStringForRealTWT

class SleepDayAdapter : RecyclerView.Adapter<SleepDayAdapter.ViewHolder>() {

    var data = listOf<SleepDay>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_day, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.item_list_date)
        val twt: TextView = itemView.findViewById(R.id.item_list_TWT)

        fun bind(item: SleepDay) {
            val holderContext = itemView.context
            val res = holderContext.resources
            date.text = getDateString(item.date, holderContext)
            twt.text = getStringForRealTWT(item.realTWT, res)
        }
    }
}