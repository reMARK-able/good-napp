package com.gmail.remarkable.development.goodnapp.util

import android.content.Context
import android.text.format.DateFormat
import java.util.*

// Convert timestamp to time in String format.
fun getTimeStringFromTimestamp(timestamp: Long, context: Context): String {
    if (timestamp == 0L) return ""
    val date = Date(timestamp)
    val df = DateFormat.getTimeFormat(context)
    return df.format(date)
}

// Convert duration in millis to string format.
fun getDurationString(millis: Long): String {
    if (millis == 0L) return ""
    val hours = millis / (60 * 60 * 1000) % 24
    val min = millis / (60 * 1000) % 60

    return "$hours hr $min min"
}

// Set the duration string for nap layout.
fun getDurationNapString(millis: Long): String =
    when {
        millis <= 0L -> "--:--" //this should get resource string!!!! (only for testing purpose)
        // here can be another scenario for validation eg. hint for the user
        else -> getDurationString(millis)
    }

// Convert timestamp to time in String format for non-editable field.
fun getStringForTargetBedtime(timestamp: Long, context: Context): String {
    if (timestamp == 0L) return "--:--"
    val date = Date(timestamp)
    val df = DateFormat.getTimeFormat(context)
    return df.format(date)
}

// Convert duration in millis to string format.
fun getStringForRealTWT(millis: Long): String {
    if (millis == 0L) return "--:--"
    val hours = millis / (60 * 60 * 1000) % 24
    val min = millis / (60 * 1000) % 60

    return "$hours hr $min min"
}

// Calculates duration in millis from picker time.
fun getDurationoFromPicker(hour: Int, min: Int): Long {
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