package com.gmail.remarkable.development.goodnapp.util

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gmail.remarkable.development.goodnapp.MAX_AWAKES_NUMBER
import com.gmail.remarkable.development.goodnapp.R
import com.gmail.remarkable.development.goodnapp.SleepDay
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("dateFormatted")
fun TextView.setDateFormatted(sleepDay: SleepDay?) {
    sleepDay?.let {
        text = when (val date = sleepDay.date) {
            getTodayInMillis() -> context.getString(R.string.today)
            prevDayDate(getTodayInMillis()) -> context.getString(R.string.yesterday)
            else -> getDateString(date, context)
        }
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
        else -> getWakeUpErrorString(sleepDay, context.resources)
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

@BindingAdapter(value = ["awakesDayString", "longVersion"], requireAll = false)
fun TextView.setAwakesDayString(sleepDay: SleepDay?, longVersion: Boolean?) {
    val version = longVersion ?: true
    val res = context.resources
    text = when {
        sleepDay == null -> res.getString(R.string.no_time)
        else -> getDurationNonEmptyString(sleepDay.realDayAwakeTime, res, version)
    }
}

@BindingAdapter(value = ["awakesNightString", "longVersion"], requireAll = false)
fun TextView.setAwakesNightString(sleepDay: SleepDay?, longVersion: Boolean?) {
    val version = longVersion ?: true
    val res = context.resources
    text = when {
        sleepDay == null -> res.getString(R.string.no_time)
        else -> getDurationNonEmptyString(
            getTotalNightAwakesTime(sleepDay.nightAwakes),
            res,
            version
        )
    }
}

@BindingAdapter(value = ["totalAwakesString", "longVersion"], requireAll = false)
fun TextView.setTotalAwakesString(sleepDay: SleepDay?, longVersion: Boolean?) {
    val version = longVersion ?: true
    val res = context.resources
    text = when {
        sleepDay == null -> res.getString(R.string.no_time)
        else -> getDurationNonEmptyString(getTotalAwakesTime(sleepDay), res, version)
    }
}

@BindingAdapter(value = ["sleepDayString", "longVersion"], requireAll = false)
fun TextView.setSleepDayString(sleepDay: SleepDay?, longVersion: Boolean?) {
    val version = longVersion ?: true
    val res = context.resources
    text = when (sleepDay) {
        null -> res.getString(R.string.no_time)
        else -> getDurationNonEmptyString(getTotalNapTime(sleepDay.naps), res, version)
    }
}

@BindingAdapter(value = ["sleepNightString", "nextDayWakeUp", "longVersion"], requireAll = false)
fun TextView.setSleepNightString(sleepDay: SleepDay?, wakeUp: Long?, longVersion: Boolean?) {
    val version = longVersion ?: true
    val res = context.resources
    text = when {
        validRealBedtime(sleepDay, res) != null -> res.getString(R.string.no_time)
        else -> getDurationNonEmptyString(getNightSleep(sleepDay, wakeUp), res, version)
    }
}

@BindingAdapter(value = ["sleepTotalString", "nextDayWakeUp", "longVersion"], requireAll = false)
fun TextView.setSleepTotalString(sleepDay: SleepDay?, wakeUp: Long?, longVersion: Boolean?) {
    val version = longVersion ?: true
    val res = context.resources
    val nightSleep = getNightSleep(sleepDay, wakeUp)
    val realBedTimeValid = validRealBedtime(sleepDay, res)
    val noTime = res.getString(R.string.no_time)
    text = when {
        sleepDay == null || wakeUp == null -> noTime
        nightSleep != 0L && realBedTimeValid == null ->
            getDurationNonEmptyString(nightSleep + getTotalNapTime(sleepDay.naps), res, version)
        else -> noTime
    }
}

@BindingAdapter("showWhenEmpty")
fun View.showWhenEmpty(data: List<Pair<SleepDay, Long?>>?) {
    visibility = when {
        data == null || data.isEmpty() -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("showIfNoToday")
fun View.showIfNoToday(data: List<Pair<SleepDay, Long?>>?) {
    visibility = when (data) {
        null -> View.GONE
        else -> isTodayAdded(data)
    }
}

@BindingAdapter("comment")
fun TextInputEditText.setComment(sleepDay: SleepDay?) {
    val newText = when (sleepDay) {
        null -> ""
        else -> sleepDay.comment
    }
    setText(newText)
}