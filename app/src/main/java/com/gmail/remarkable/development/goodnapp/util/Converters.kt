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
    df.timeZone = TimeZone.getTimeZone("UTC")
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
    df.timeZone = TimeZone.getTimeZone("UTC")
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

// Method to set date field in SleepDay object with local "now date" but written to UTC 00:00 time.
fun getTodayInMillis(): Long {
    val calendarUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val calendarLocal = Calendar.getInstance()

    val localYear = calendarLocal.get(Calendar.YEAR)
    val localMonth = calendarLocal.get(Calendar.MONTH)
    val localDay = calendarLocal.get(Calendar.DAY_OF_MONTH)

    calendarUTC.set(Calendar.YEAR, localYear)
    calendarUTC.set(Calendar.MONTH, localMonth)
    calendarUTC.set(Calendar.DAY_OF_MONTH, localDay)
    calendarUTC.set(Calendar.HOUR_OF_DAY, 0)
    calendarUTC.set(Calendar.MINUTE, 0)
    calendarUTC.set(Calendar.SECOND, 0)
    calendarUTC.set(Calendar.MILLISECOND, 0)

    return calendarUTC.timeInMillis
}

// Convert date from timestamp format to local String format according to UTC.
fun getDateString(timestampDate: Long, context: Context): String {
    if (timestampDate == 0L) return "unknown date!!!"
    val date = Date(timestampDate)
    val df = DateFormat.getLongDateFormat(context)
    df.timeZone = TimeZone.getTimeZone("UTC")
    return df.format(date)
}

// Method to set current date to the SleepDay
private fun getCurrentDate(): String {
    val c = Calendar.getInstance()
    val day = c.get(Calendar.DAY_OF_MONTH)
    val month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
    val year = c.get(Calendar.YEAR)
    return "$day $month $year"
}