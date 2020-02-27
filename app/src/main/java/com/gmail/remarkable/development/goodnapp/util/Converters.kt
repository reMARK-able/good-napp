package com.gmail.remarkable.development.goodnapp.util

import android.content.Context
import android.content.res.Resources
import android.text.format.DateFormat
import com.gmail.remarkable.development.goodnapp.R
import com.gmail.remarkable.development.goodnapp.SleepDay
import com.gmail.remarkable.development.goodnapp.SleepDay.Nap
import com.gmail.remarkable.development.goodnapp.SleepDay.NightAwake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

// Convert timestamp to time in String format.
fun getTimeStringFromTimestamp(timestamp: Long, context: Context): String {
    if (timestamp == 0L) return ""
    val date = Date(timestamp)
    val df = DateFormat.getTimeFormat(context)
    df.timeZone = TimeZone.getTimeZone("UTC")
    return df.format(date)
}

/**
 * Convert duration in millis to string format.
 * @param millis duration in milliseconds.
 *
 * @param longVersion default true for long format.
 * @return String in long format eg. "12 hr 15 min" or short format eg. "12:15".
 */
fun getDurationString(millis: Long, resources: Resources, longVersion: Boolean = true): String {
    if (millis == 0L) return ""
    val hours = millis / (60 * 60 * 1000) % 24
    val min = millis / (60 * 1000) % 60
    return if (longVersion) resources.getString(R.string.time_duration_format, hours, min)
    else resources.getString(R.string.time_duration_format_short, hours, min)
}

fun getAllAwakeTimesString(awakeList: List<Long>, res: Resources): String {
    val emptyTime = res.getString(R.string.no_time_forAllAwakeTimeString)
    val separator = res.getString(R.string.separator_for_allAwakeTimesString)
    return when {
        awakeList.isNullOrEmpty() -> res.getString(R.string.notAvailable_short)
        else -> buildString {
            for ((index, awakeTime) in awakeList.withIndex()) {
                if (awakeTime == 0L) append(emptyTime)
                else append(getDurationString(awakeTime, res, false))
                if (index < awakeList.lastIndex) {
                    append(separator)
                }
            }

        }
    }
}

// Set the duration string for nap layout.
fun getDurationNonEmptyString(millis: Long, resources: Resources): String =
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

// Gets total night awakes time.
fun getTotalNightAwakesTime(nightAwakes: List<NightAwake>): Long {
    var sum = 0L
    for (awake in nightAwakes) sum += awake.duration
    return sum
}

// Gets total nap sleep time.
fun getTotalNapTime(naps: List<Nap>): Long {
    var sum = 0L
    for (nap in naps) sum += nap.duration
    return sum
}

// Gets total (day + night) awake time.
fun getTotalAwakesTime(sleepDay: SleepDay?): Long {
    return when {
        sleepDay == null -> 0
        sleepDay.realDayAwakeTime == 0L -> 0
        else -> sleepDay.realDayAwakeTime + getTotalNightAwakesTime(sleepDay.nightAwakes)
    }
}

// Calculates duration in millis from picker time.
fun getDurationFromPicker(hour: Int, min: Int): Long {
    return ((hour * 60) + min) * 60000L
}

// Method to set date field in SleepDay object with local "now date" but written to UTC 00:00 time.
fun getTodayInMillis(): Long {
    val calendarUTC = getUTCCalendar()
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
    if (timestampDate == 0L) return context.resources.getString(R.string.unknown_date)
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

// Method to set fake List<SleepDay> with dates since 2 January 2020.
fun setFakeData(): List<SleepDay> {
    val calendarUTC = getUTCCalendar()
    calendarUTC.timeInMillis = 1577836800000
    val dateList = MutableList(31) {
        calendarUTC.add(Calendar.DATE, 1)
        calendarUTC.timeInMillis
    }
    val fakeDaysList = mutableListOf<SleepDay>()
    for (date in dateList) {
        val newDay = SleepDay(mutableListOf())
        newDay.date = date
        fakeDaysList.add(newDay)
    }
    return fakeDaysList.reversed()
}

private fun getUTCCalendar() = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

// Get the previous date in millis UTC format.
fun prevDayDate(date: Long): Long {
    val calendarUTC = getUTCCalendar()
    calendarUTC.timeInMillis = date
    calendarUTC.add(Calendar.DATE, -1)
    return calendarUTC.timeInMillis
}

/**
 * Makes list of pairs (SleepDay with previous SleepDay on the list) for recyclerView adapter.
 * @param list List to convert.
 * @return List<Pair<sleepday, previousSleepDay>
 */
suspend fun makePairs(list: List<SleepDay>?): List<Pair<SleepDay, SleepDay?>> {
    return withContext(Dispatchers.Default) {
        when {
            list.isNullOrEmpty() -> listOf()
            list.size == 1 -> listOf(list[0] to null)
            else -> list.zipWithNext() + listOf(list.last() to null)
        }

    }
}

/**
 * Gets next UTC day from timestamp.
 */
fun Long.nextDay(): Long {
    val calendarUTC = getUTCCalendar()
    calendarUTC.timeInMillis = this
    calendarUTC.add(Calendar.DATE, 1)
    return calendarUTC.timeInMillis
}