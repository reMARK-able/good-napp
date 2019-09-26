package com.gmail.remarkable.development.goodnapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class DayViewModel : ViewModel() {

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

        nap1Start.value = ""
        nap1End.value = ""
    }

    fun onTimeSet(viewId: String, hour: Int, minutes: Int, time: Long) {

        when (viewId) {

//            NAP_1_START -> nap1Start.value = time
//            NAP_1_END -> nap1End.value = time
//            NAP_2_START -> nap2Start.value = time
//            NAP_2_END -> nap2End.value = time
//            NAP_3_START -> nap3Start.value = time
//            NAP_3_END -> nap3End.value = time
//            NAP_4_START -> nap4Start.value = time
//            NAP_4_END -> nap4End.value = time
//            NAP_5_START -> nap5Start.value = time
//            NAP_5_END -> nap5End.value = time
        }
        Log.i("DayViewModel", "Twt i String == ${getDurationString(time)}")
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
        val date = Date(millis)
        val sdf = SimpleDateFormat("H'hr' m'min'")
        return sdf.format(date)
    }

    // Calculates duration in millis from picker time.
    fun getDurationoFromPicker(hour: Int, min: Int): Long {
        return ((hour * 60) + min) * 60000L
    }

    // Convert timestamp to time in String format.
    fun getTimeFromTimeStamp(timestamp: Long): String {
        if (timestamp == 0L) return ""
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("DayViewModel", "DayViewModel is destroyed.")
    }
}