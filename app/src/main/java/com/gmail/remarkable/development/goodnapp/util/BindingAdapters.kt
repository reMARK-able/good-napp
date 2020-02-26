package com.gmail.remarkable.development.goodnapp.util

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gmail.remarkable.development.goodnapp.MAX_AWAKES_NUMBER
import com.gmail.remarkable.development.goodnapp.SleepDay
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("dateFormatted")
fun TextView.setDateFormatted(sleepDay: SleepDay?) {
    sleepDay?.let {
        text = getDateString(sleepDay.date, context)
    }
}

@BindingAdapter("realTWTFormatted")
fun TextView.setRealTWTFormatted(sleepDay: SleepDay?) {
    sleepDay?.let {
        text = getStringForRealTWT(sleepDay.realDayAwakeTime, context.resources)
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

@BindingAdapter("awakeStartError", "index")
fun TextInputLayout.setAwakeStartError(sleepDay: SleepDay?, index: Int) {
    error = if (sleepDay == null) null
    else validAwakeStart(sleepDay, index, context.resources)
}

@BindingAdapter("awakeEndError", "index")
fun TextInputLayout.setAwakeEndError(sleepDay: SleepDay?, index: Int) {
    error = if (sleepDay == null) null
    else validAwakeEnd(sleepDay, index, context.resources)
}

// Sets awake button enable if all nightAwake are valid.
@BindingAdapter("awakeButtonEnable")
fun Button.setAwakeButtonEnable(sleepDay: SleepDay?) {
    isEnabled = when {
        sleepDay == null -> false
        sleepDay.nightAwakes.size >= MAX_AWAKES_NUMBER -> false
        else -> validateAllNightAwakes(sleepDay, context.resources)
    }
}