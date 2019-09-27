package com.gmail.remarkable.development.goodnapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class DayViewModel : ViewModel() {

    val mLiveSleepDay = MutableLiveData<SleepDay>()
    val mDay = SleepDay(date = getCurrentDate())

    val nap1Start = MutableLiveData<String>()
    val nap1End = MutableLiveData<String>()
    val nap2Start = MutableLiveData<String>()
    val nap2End = MutableLiveData<String>()
    val nap3Start = MutableLiveData<String>()
    val nap3End = MutableLiveData<String>()
    val nap4Start = MutableLiveData<String>()
    val nap4End = MutableLiveData<String>()
    val nap5Start = MutableLiveData<String>()
    val nap5End = MutableLiveData<String>()


    init {
        Log.i("DayViewModel", "DayViewModel is created.")

        mLiveSleepDay.value = mDay
    }

    fun onTimeSet(viewId: String, hour: Int, minutes: Int, timestamp: Long) {

        when (viewId) {

            TARGET_TWT -> mDay.targetTWT = getDurationoFromPicker(hour, minutes)
            WAKE_UP -> mDay.wakeUp = timestamp
            OUT_OF_BED -> mDay.outOfBed = timestamp
            REAL_BEDTIME -> mDay.realBedtime = timestamp
            NAP_1_START -> mDay.naps[0].start = timestamp
            NAP_1_END -> mDay.naps[0].end = timestamp
            NAP_2_START -> mDay.naps[1].start = timestamp
            NAP_2_END -> mDay.naps[1].end = timestamp
            NAP_3_START -> mDay.naps[2].start = timestamp
            NAP_3_END -> mDay.naps[2].end = timestamp
            NAP_4_START -> mDay.naps[3].start = timestamp
            NAP_4_END -> mDay.naps[3].end = timestamp
            NAP_5_START -> mDay.naps[4].start = timestamp
            NAP_5_END -> mDay.naps[4].end = timestamp
        }
        // Refresh data in LiveData
        mLiveSleepDay.value = mDay
        val currentDuration = getDurationoFromPicker(hour, minutes)
        Log.i("DayViewModel", "timestamp== $timestamp")
        Log.i("DayViewModel", "getDurationFromPicker== $currentDuration")
        Log.i("DayViewModel", "getDurationString== ${getDurationString(currentDuration)}")

    }

    // Method to set current date to the SleepDay
    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val year = c.get(Calendar.YEAR)
        return "$day $month $year"
    }

    // Convert duration in millis to string format.
    fun getDurationString(millis: Long): String {
        if (millis == 0L) return ""
        val hours = millis / (60 * 60 * 1000) % 24
        val min = millis / (60 * 1000) % 60

        return "$hours hr $min min"

    }

    // Calculates duration in millis from picker time.
    fun getDurationoFromPicker(hour: Int, min: Int): Long {
        return ((hour * 60) + min) * 60000L
    }

    // Convert timestamp to time in String format.
    fun getTimeStringFromTimestamp(timestamp: Long): String {
        if (timestamp == 0L) return ""
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    // Set the duration string for nap layout.
    fun setDurationNapString(millis: Long): String =
        when {
            millis <= 0L -> "--:--" //this should get resource string!!!! (only for testing purpose)
            // here can be another scenario for validation eg. hint for the user
            else -> getDurationString(millis)
        }


    override fun onCleared() {
        super.onCleared()
        Log.i("DayViewModel", "DayViewModel is destroyed.")
    }
}