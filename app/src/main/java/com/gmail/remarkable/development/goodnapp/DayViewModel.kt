package com.gmail.remarkable.development.goodnapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.gmail.remarkable.development.goodnapp.SleepDay.Nap
import java.text.SimpleDateFormat
import java.util.*

private const val MAX_NAPS_NUMBER = 5

class DayViewModel : ViewModel() {

    private val _mLiveSleepDay = MutableLiveData<SleepDay>()
    val mLiveSleepDay: LiveData<SleepDay>
        get() = _mLiveSleepDay

    val mDay = SleepDay(date = getCurrentDate())

    // LiveData to set Add nap button enabled or disabled.
    val isAllDataValid = Transformations.map(mLiveSleepDay) { areNapsValid() }

//    Previous version
//    val nap1Start = MutableLiveData<String>()
//    val nap1End = MutableLiveData<String>()
//    val nap2Start = MutableLiveData<String>()
//    val nap2End = MutableLiveData<String>()
//    val nap3Start = MutableLiveData<String>()
//    val nap3End = MutableLiveData<String>()
//    val nap4Start = MutableLiveData<String>()
//    val nap4End = MutableLiveData<String>()
//    val nap5Start = MutableLiveData<String>()
//    val nap5End = MutableLiveData<String>()


    init {
        Log.i("DayViewModel", "DayViewModel is created.")

        _mLiveSleepDay.value = mDay
    }

    // Adds another nap to the SleepDay object.
    fun addNap() {
        if (mDay.naps.size < MAX_NAPS_NUMBER) {
            val currentNapsNumb = mDay.naps.size

            if (currentNapsNumb == 0 || areNapsValid()) {
                mDay.naps.add(Nap())
                _mLiveSleepDay.value = mDay
            }
        }
    }

    // Returns true if all fields in naps are correct.
    fun areNapsValid(): Boolean {
        for ((index, nap) in mDay.naps.withIndex()) {
            if (!(nap.start != 0L && nap.end != 0L
                        && validNapStart(mDay.naps, index) == null
                        && validNapEnd(mDay.naps, index) == null)
            ) return false
        }
        return true
    }

    // Deletes the nap with index from a view.
    fun deleteNap(index: Int) {
        mDay.naps.removeAt(index)
        _mLiveSleepDay.value = mDay

    }

    // For validation the start of the nap.
    fun validNapStart(naps: List<Nap>, index: Int): String? {
        if (index >= naps.size || naps[index].start == 0L) return null
        else {
            val start = naps[index].start
            var i = index
            do {
                val prevNap: Nap? = naps.getOrNull(i - 1)
                when {
                    start - mDay.outOfBed <= 0L -> return "earlier than outOfBed"
                    prevNap != null && start <= prevNap.start -> return "earlier than prevNap start"
                    prevNap != null && start <= prevNap.end -> return "earlier than prevNap end"
                }
                i -= 1
            } while (prevNap != null)
            return null
        }
    }

    // For validation the end of the nap.
    fun validNapEnd(naps: List<Nap>, index: Int): String? {
        if (index >= naps.size || naps[index].end == 0L) return null
        else {
            val end = naps[index].end
            val start = naps[index].start
            var i = index
            do {
                val prevNap: Nap? = naps.getOrNull(i - 1)
                when {
                    end - mDay.outOfBed <= 0L -> return "earlier than outOfBed"
                    start != 0L && end <= start -> return "must be later than start"
                    prevNap != null && end <= prevNap.start -> return "earlier than prevNap start"
                    prevNap != null && end <= prevNap.end -> return "earlier than prevNap end"
                    mDay.realBedtime != 0L && end >= mDay.realBedtime -> return "later than realBedtime"
                }
                i -= 1
            } while (prevNap != null)
            return null
        }
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
        _mLiveSleepDay.value = mDay
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

    // Convert timestamp to time in String format.
    fun getTimeStringFromTimestamp(timestamp: Long): String {
        if (timestamp == 0L) return ""
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    // Convert timestamp to time in String format for non-editable field.
    fun getStringForTargetBedtime(timestamp: Long): String {
        if (timestamp == 0L) return "--:--"
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    // Set the duration string for nap layout.
    fun getDurationNapString(millis: Long): String =
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