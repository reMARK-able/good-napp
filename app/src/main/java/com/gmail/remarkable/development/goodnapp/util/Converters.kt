package com.gmail.remarkable.development.goodnapp.util

import android.content.Context
import android.content.res.Resources
import android.text.format.DateFormat
import com.gmail.remarkable.development.goodnapp.R
import java.util.*

// Convert timestamp to time in String format.
fun getTimeStringFromTimestamp(timestamp: Long, context: Context): String {
    if (timestamp == 0L) return ""
    val date = Date(timestamp)
    val df = DateFormat.getTimeFormat(context)
    return df.format(date)
}

// Convert duration in millis to string format.
fun getDurationString(millis: Long, resources: Resources): String {
    if (millis == 0L) return ""
    val hours = millis / (60 * 60 * 1000) % 24
    val min = millis / (60 * 1000) % 60

    return resources.getString(R.string.time_duration_format, hours, min)
}

// Set the duration string for nap layout.
fun getDurationNapString(millis: Long, resources: Resources): String =
    when {
        millis <= 0L -> resources.getString(R.string.no_time)
        // here can be another scenario for validation eg. hint for the user
        else -> getDurationString(millis, resources)
    }

// Convert timestamp to time in String format for non-editable field.
fun getStringForTargetBedtime(timestamp: Long, context: Context): String {
    if (timestamp == 0L) return context.resources.getString(R.string.no_time)
    val date = Date(timestamp)
    val df = DateFormat.getTimeFormat(context)
    return df.format(date)
}

// Convert duration in millis to string format.
fun getStringForRealTWT(millis: Long, resources: Resources): String {
    if (millis == 0L) return resources.getString(R.string.no_time)
    val hours = millis / (60 * 60 * 1000) % 24
    val min = millis / (60 * 1000) % 60

    return resources.getString(R.string.time_duration_format, hours, min)
}

// Calculates duration in millis from picker time.
fun getDurationFromPicker(hour: Int, min: Int): Long {
    return ((hour * 60) + min) * 60000L
}

// Method to set current date to the SleepDay
private fun getCurrentDate(): String {
    val c = Calendar.getInstance()
    val day = c.get(Calendar.DAY_OF_MONTH)
    val month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
    val year = c.get(Calendar.YEAR)
    return "$day $month $year"
}