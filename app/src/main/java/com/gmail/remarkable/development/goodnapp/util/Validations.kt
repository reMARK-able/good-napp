package com.gmail.remarkable.development.goodnapp.util

import android.content.res.Resources
import android.view.View
import com.gmail.remarkable.development.goodnapp.R
import com.gmail.remarkable.development.goodnapp.SleepDay

/**
 * Method For validation the start of the naps.
 */
fun validNapStart(mDay: SleepDay?, index: Int, resources: Resources): String? {
    if (mDay == null) return null
    val naps = mDay.naps
    if (index >= naps.size || naps[index].start == 0L) return null
    else {
        val start = naps[index].start
        var i = index
        do {
            val prevNap: SleepDay.Nap? = naps.getOrNull(i - 1)
            when {
                start - mDay.outOfBed <= 0L -> return resources.getString(R.string.error_must_be_later_than_out_of_bed)
                prevNap != null && start <= prevNap.start -> return resources.getString(R.string.error_must_be_later_than_prev_nap_start)
                prevNap != null && start <= prevNap.end -> return resources.getString(R.string.error_must_be_later_than_prev_nap_end)
            }
            i -= 1
        } while (prevNap != null)
        return null
    }
}

/**
 * Method for validation the end of the nap.
 */
fun validNapEnd(mDay: SleepDay?, index: Int, resources: Resources): String? {
    if (mDay == null) return null
    val naps = mDay.naps
    if (index >= naps.size || naps[index].end == 0L) return null
    else {
        val end = naps[index].end
        val start = naps[index].start
        var i = index
        do {
            val prevNap: SleepDay.Nap? = naps.getOrNull(i - 1)
            when {
                end - mDay.outOfBed <= 0L -> return resources.getString(R.string.error_must_be_later_than_out_of_bed)
                start != 0L && end <= start -> return resources.getString(R.string.error_must_be_later_than_start)
                prevNap != null && end <= prevNap.start -> return resources.getString(R.string.error_must_be_later_than_prev_nap_start)
                prevNap != null && end <= prevNap.end -> return resources.getString(R.string.error_must_be_later_than_prev_nap_end)
                mDay.realBedtime != 0L && end >= mDay.realBedtime -> return resources.getString(R.string.error_cant_be_later_than_realBedTime)
            }
            i -= 1
        } while (prevNap != null)
        return null
    }
}

/**
 * Method for validation the start of the nightAwake.
 */
fun validAwakeStart(mDay: SleepDay?, index: Int, resources: Resources): String? {
    if (mDay == null) return null
    val nightAwakes = mDay.nightAwakes
    if (index >= nightAwakes.size || nightAwakes[index].start == 0L) return null
    else {
        val start = nightAwakes[index].start
        var i = index
        val lastNapEnd = mDay.naps.getOrNull(mDay.naps.lastIndex)?.end ?: 0
        do {
            val prevAwake = nightAwakes.getOrNull(i - 1)
            when {
                start - mDay.outOfBed <= 0L -> return resources.getString(R.string.error_must_be_later_than_out_of_bed)
                start - mDay.realBedtime <= 0L -> return resources.getString(R.string.error_must_be_later_than_realBedTime)
                start - lastNapEnd <= 0L -> return resources.getString(R.string.error_must_be_later_than_last_nap_end)
                prevAwake != null && start <= prevAwake.start -> return resources.getString(R.string.error_must_be_later_than_prev_awake_start)
                prevAwake != null && start <= prevAwake.end -> return resources.getString(R.string.error_must_be_later_than_prev_awake_end)
            }
            i -= 1
        } while (prevAwake != null)
        return null
    }
}

/**
 * Method for validation the end of the nightAwake.
 */
fun validAwakeEnd(mDay: SleepDay?, index: Int, resources: Resources): String? {
    if (mDay == null) return null
    val awakes = mDay.nightAwakes
    if (index >= awakes.size || awakes[index].end == 0L) return null
    else {
        val end = awakes[index].end
        val start = awakes[index].start
        val lastNapEnd = mDay.naps.getOrNull(mDay.naps.lastIndex)?.end ?: 0
        var i = index
        do {
            val prevAwake = awakes.getOrNull(i - 1)
            when {
                end - mDay.outOfBed <= 0L -> return resources.getString(R.string.error_must_be_later_than_out_of_bed)
                end - mDay.realBedtime <= 0L -> return resources.getString(R.string.error_must_be_later_than_realBedTime)
                end - lastNapEnd <= 0L -> return resources.getString(R.string.error_must_be_later_than_last_nap_end)
                start != 0L && end <= start -> return resources.getString(R.string.error_must_be_later_than_start)
                prevAwake != null && end <= prevAwake.start -> return resources.getString(R.string.error_must_be_later_than_prev_awake_start)
                prevAwake != null && end <= prevAwake.end -> return resources.getString(R.string.error_must_be_later_than_prev_awake_end)
            }
            i -= 1
        } while (prevAwake != null)
        return null
    }
}

/**
 * Method for validation of outOfBed field.
 */
fun validOutOfBed(mDay: SleepDay?, resources: Resources): String? {
    if (mDay == null) return null
    val oob = mDay.outOfBed
    val firstNap = mDay.naps.getOrNull(0)
    if (oob != 0L && mDay.realBedtime != 0L && mDay.realBedtime <= oob) {
        return resources.getString(R.string.error_cant_be_later_than_real_bedtime)
    }
    if (firstNap != null && oob != 0L) {
        return when {
            firstNap.start != 0L && firstNap.start <= oob -> resources.getString(R.string.error_cant_be_later_than_first_nap)
            firstNap.end != 0L && firstNap.end <= oob -> resources.getString(R.string.error_conflict_with_first_nap_end)
            else -> null
        }
    }
    return null
}

/**
 * Method for validation of targetBedtime field.
 */
fun validRealBedtime(mDay: SleepDay?, resources: Resources): String? {
    if (mDay == null) return null
    val rbt = mDay.realBedtime
    val lastNap = mDay.naps.lastOrNull()
    if (rbt != 0L && mDay.outOfBed != 0L && mDay.outOfBed >= rbt) {
        return resources.getString(R.string.error_must_be_later_than_out_of_bed)
    }
    if (lastNap != null && rbt != 0L) {
        return when {
            lastNap.start != 0L && lastNap.start >= rbt -> resources.getString(R.string.error_must_be_later_than_last_nap_start)
            lastNap.end != 0L && lastNap.end >= rbt -> resources.getString(R.string.error_must_be_later_than_last_nap_end)
            else -> null
        }
    }
    return null
}

/**
 * Returns true if all fields are valid to set the Add Nap button enable.
 */
fun validateData(mDay: SleepDay?, resources: Resources): Boolean {
    if (mDay == null) return false
    // Returns true is all naps are valid.
    fun validateNaps(): Boolean {
        for ((index, nap) in mDay.naps.withIndex()) {
            if (!(nap.start != 0L && nap.end != 0L
                        && validNapStart(mDay, index, resources) == null
                        && validNapEnd(mDay, index, resources) == null)
            ) return false
        }
        return true
    }
    return validOutOfBed(mDay, resources) == null && validRealBedtime(
        mDay,
        resources
    ) == null && validateNaps()
}

// Check if latest day in recycler's list is equal with today.
fun isTodayAdded(days: List<SleepDay>?): Int {
    return when {
        days == null -> View.GONE
        days.isEmpty() -> View.VISIBLE
        days[0].date == getTodayInMillis() -> View.GONE
        else -> View.VISIBLE
    }
}

/**
 * Returns true if all nightAwakes are valid to set "Night awake" button enable.
 */
fun validateAllNightAwakes(sleepDay: SleepDay, resources: Resources): Boolean {
    for ((index, awake) in sleepDay.nightAwakes.withIndex()) {
        if (awake.start == 0L || awake.end == 0L ||
            validAwakeStart(sleepDay, index, resources) != null ||
            validAwakeEnd(sleepDay, index, resources) != null
        ) return false
    }
    return true
}