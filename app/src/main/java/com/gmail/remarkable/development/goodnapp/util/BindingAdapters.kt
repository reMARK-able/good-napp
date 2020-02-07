package com.gmail.remarkable.development.goodnapp.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gmail.remarkable.development.goodnapp.SleepDay

@BindingAdapter("dateFormatted")
fun TextView.setDateFormatted(sleepDay: SleepDay?) {
    sleepDay?.let {
        text = getDateString(sleepDay.date, context)
    }
}

@BindingAdapter("realTWTFormatted")
fun TextView.setRealTWTFormatted(sleepDay: SleepDay?) {
    sleepDay?.let {
        text = getStringForRealTWT(sleepDay.realTWT, context.resources)
    }
}