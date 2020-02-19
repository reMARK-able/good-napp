package com.gmail.remarkable.development.goodnapp.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gmail.remarkable.development.goodnapp.SleepDay


@BindingAdapter("awakeTimesFormatted")
fun TextView.setAwakeTimesFormatted(sleepDay: SleepDay?) {
    text = when (sleepDay) {
        null -> "n/a"
        else -> getAllAwakeTimesString(sleepDay.awakeTimes, context.resources)
    }
}