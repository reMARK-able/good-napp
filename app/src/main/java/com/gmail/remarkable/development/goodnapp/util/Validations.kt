package com.gmail.remarkable.development.goodnapp.util

import com.gmail.remarkable.development.goodnapp.SleepDay

/**
 * Method For validation the start of the naps.
 */
fun validNapStart(mDay: SleepDay?, index: Int): String? {
    if (mDay == null) return null
    val naps = mDay.naps
    if (index >= naps.size || naps[index].start == 0L) return null
    else {
        val start = naps[index].start
        var i = index
        do {
            val prevNap: SleepDay.Nap? = naps.getOrNull(i - 1)
            when {
                start - mDay.outOfBed <= 0L -> return "Must be later than outOfBed."
                prevNap != null && start <= prevNap.start -> return "Must be later than previous nap."
                prevNap != null && start <= prevNap.end -> return "Must be later than previous nap."
            }
            i -= 1
        } while (prevNap != null)
        return null
    }
}

/**
 * Method for validation the end of the nap.
 */
fun validNapEnd(mDay: SleepDay?, index: Int): String? {
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
                end - mDay.outOfBed <= 0L -> return "Must be later than out of bed."
                start != 0L && end <= start -> return "Must be later than start."
                prevNap != null && end <= prevNap.start -> return "Must be later than previous nap."
                prevNap != null && end <= prevNap.end -> return "Must be later than previous nap."
                mDay.realBedtime != 0L && end >= mDay.realBedtime -> return "Can't be later than realBedtime."
            }
            i -= 1
        } while (prevNap != null)
        return null
    }
}

/**
 * Method for validation of outOfBed field.
 */
fun validOutOfBed(mDay: SleepDay?): String? {
    if (mDay == null) return null
    val oob = mDay.outOfBed
    val firstNap = mDay.naps.getOrNull(0)
    if (oob != 0L && mDay.realBedtime != 0L && mDay.realBedtime <= oob) {
        return "Can't be later than real bedtime."
    }
    if (firstNap != null && oob != 0L) {
        return when {
            firstNap.start != 0L && firstNap.start <= oob -> "Can't be later than first nap."
            firstNap.end != 0L && firstNap.end <= oob -> "Conflict with first nap end."
            else -> null
        }
    }
    return null
}

/**
 * Method for validation of targetBedtime field.
 */
fun validRealBedtime(mDay: SleepDay?): String? {
    if (mDay == null) return null
    val rbt = mDay.realBedtime
    val lastNap = mDay.naps.lastOrNull()
    if (rbt != 0L && mDay.outOfBed != 0L && mDay.outOfBed >= rbt) {
        return "Must be later than out of bed."
    }
    if (lastNap != null && rbt != 0L) {
        return when {
            lastNap.start != 0L && lastNap.start >= rbt -> "Must be later than last nap start."
            lastNap.end != 0L && lastNap.end >= rbt -> "Must be later than last nap end."
            else -> null
        }
    }
    return null
}

/**
 * Returns true if all fields are valid to set the Add Nap button enable.
 */
fun validateData(mDay: SleepDay?): Boolean {
    if (mDay == null) return false
    // Returns true is all naps are valid.
    fun validateNaps(): Boolean {
        for ((index, nap) in mDay.naps.withIndex()) {
            if (!(nap.start != 0L && nap.end != 0L
                        && validNapStart(mDay, index) == null
                        && validNapEnd(mDay, index) == null)
            ) return false
        }
        return true
    }
    return validOutOfBed(mDay) == null && validRealBedtime(mDay) == null && validateNaps()
}