package com.gmail.remarkable.development.goodnapp.util

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gmail.remarkable.development.goodnapp.MAX_AWAKES_NUMBER
import com.gmail.remarkable.development.goodnapp.R
import com.gmail.remarkable.development.goodnapp.SleepDay
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("dateFormatted")
fun TextView.setDateFormatted(sleepDay: SleepDay?) {
    sleepDay?.let {
        text = getDateString(sleepDay.date, context)
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
        text = getDurationNonEmptyString(it.duration, context.resources)
    }
}

@BindingAdapter("wakeUpError")
fun TextInputLayout.setWakeUpError(sleepDay: SleepDay?) {
    error = when (sleepDay) {
        null -> null
        else -> validWakeUp(sleepDay, context.resources)
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

@BindingAdapter("awakesDayString")
fun TextView.setAwakesDayString(sleepDay: SleepDay?) {
    val res = context.resources
    text = when {
        sleepDay == null -> res.getString(R.string.no_time)
        else -> getDurationNonEmptyString(sleepDay.realDayAwakeTime, res)
    }
}

@BindingAdapter("awakesNightString")
fun TextView.setAwakesNightString(sleepDay: SleepDay?) {
    val res = context.resources
    text = when {
        sleepDay == null -> res.getString(R.string.no_time)
        else -> getDurationNonEmptyString(getTotalNightAwakesTime(sleepDay.nightAwakes), res)
    }
}

@BindingAdapter("totalAwakesString")
fun TextView.setTotalAwakesString(sleepDay: SleepDay?) {
    val res = context.resources
    text = when {
        sleepDay == null -> res.getString(R.string.no_time)
        else -> getDurationNonEmptyString(getTotalAwakesTime(sleepDay), res)
    }
}

@BindingAdapter("sleepDayString")
fun TextView.setSleepDayString(sleepDay: SleepDay?) {
    val res = context.resources
    text = when (sleepDay) {
        null -> res.getString(R.string.no_time)
        else -> getDurationNonEmptyString(getTotalNapTime(sleepDay.naps), res)
    }
}

@BindingAdapter("sleepNightString", "nextDayWakeUp")
fun TextView.setSleepNightString(sleepDay: SleepDay?, wakeUp: Long?) {
    val res = context.resources
    text = when {
        validRealBedtime(sleepDay, res) != null -> res.getString(R.string.no_time)
        else -> getDurationNonEmptyString(getNightSleep(sleepDay, wakeUp), context.resources)
    }
}

@BindingAdapter("sleepTotalString", "nextDayWakeUp")
fun TextView.setSleepTotalString(sleepDay: SleepDay?, wakeUp: Long?) {
    val res = context.resources
    val nightSleep = getNightSleep(sleepDay, wakeUp)
    val realBedTimeValid = validRealBedtime(sleepDay, res)
    val noTime = res.getString(R.string.no_time)
    text = when {
        sleepDay == null || wakeUp == null -> noTime
        nightSleep != 0L && realBedTimeValid == null ->
            getDurationNonEmptyString(nightSleep + getTotalNapTime(sleepDay.naps), res)
        else -> noTime
    }
}