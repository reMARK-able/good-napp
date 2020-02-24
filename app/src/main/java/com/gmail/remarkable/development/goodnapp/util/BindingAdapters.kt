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

@BindingAdapter("nightAwakeStartString", "index")
fun TextView.setNightAwakeStartStringFormatted(sleepDay: SleepDay?, index: Int) {
    sleepDay?.nightAwakes?.getOrNull(index)?.let {
        text = getTimeStringFromTimestamp(it.start, context)
    }
}

@BindingAdapter("nightAwakeEndString", "index")
fun TextView.setNightAwakeEndStringFormatted(sleepDay: SleepDay?, index: Int) {
    sleepDay?.nightAwakes?.getOrNull(index)?.let {
        text = getTimeStringFromTimestamp(it.end, context)
    }
}

@BindingAdapter("nightAwakeDurationString", "index")
fun TextView.setNightAwakeDurationString(sleepDay: SleepDay?, index: Int) {
    sleepDay?.nightAwakes?.getOrNull(index)?.let {
        text = getDurationNapString(it.duration, context.resources)
    }
}