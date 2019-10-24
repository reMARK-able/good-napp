package com.gmail.remarkable.development.goodnapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.gmail.remarkable.development.goodnapp.SleepDay.Nap
import com.gmail.remarkable.development.goodnapp.database.SleepDatabaseDao
import com.gmail.remarkable.development.goodnapp.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

private const val MAX_NAPS_NUMBER = 5

class DayViewModel(
    val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _mLiveSleepDay = MutableLiveData<SleepDay>()
    val mLiveSleepDay: LiveData<SleepDay>
        get() = _mLiveSleepDay

    var mDay = SleepDay()

    private val resources = application.resources

    // LiveData to set Add nap button enabled or disabled.
    val isAllDataValid = Transformations.map(mLiveSleepDay) { day -> validateData(day, resources) }

    // LiveData for outOfBed validation.
    val isOutOfBedValid =
        Transformations.map(mLiveSleepDay) { day -> validOutOfBed(day, resources) }

    // LiveData for realBedtime validation.
    val isRealBedtimeValid =
        Transformations.map(mLiveSleepDay) { day -> validRealBedtime(day, resources) }

    // LiveData for napStart validation.
    fun hasNapStartError(index: Int) =
        Transformations.map(mLiveSleepDay) { day -> validNapStart(day, index, resources) }

    // LiveData for napEnd validation.
    fun hasNapEndError(index: Int) =
        Transformations.map(mLiveSleepDay) { day -> validNapEnd(day, index, resources) }

    // LiveData for outOfBed text field.
    val outOfBedString =
        Transformations.map(mLiveSleepDay) { day -> getTimeStringFromTimestamp(day.outOfBed) }

    // LiveData for targetTWT field.
    val targetTWTString =
        Transformations.map(mLiveSleepDay) { day -> getDurationString(day.targetTWT) }

    // LiveData for outOfBed field.
    val wakeUpString =
        Transformations.map(mLiveSleepDay) { day -> getTimeStringFromTimestamp(day.wakeUp) }

    // LiveData for nap duration field.
    fun napDurationString(index: Int) = Transformations.map(mLiveSleepDay) { day ->
        getDurationNapString(
            day.naps.getOrNull(index)?.duration ?: 0
        )
    }

    // LiveData for nap start field.
    fun napStartString(index: Int) = Transformations.map(mLiveSleepDay) { day ->
        getTimeStringFromTimestamp(
            day.naps.getOrNull(index)?.start ?: 0
        )
    }

    // LiveData for nap end field.
    fun napEndString(index: Int) = Transformations.map(mLiveSleepDay) { day ->
        getTimeStringFromTimestamp(
            day.naps.getOrNull(index)?.end ?: 0
        )
    }

    // LiveData for awake time fields.
    fun awakeTimeString(duration: Long) = Transformations.map(mLiveSleepDay) {
        getDurationString(duration)
    }

    // LiveData for target bedtime.
    val targetBedtimeString =
        Transformations.map(mLiveSleepDay) { day -> getStringForTargetBedtime(day.targetBedtime) }

    // LiveData for real bedtime field.
    val realBedtimeString =
        Transformations.map(mLiveSleepDay) { day -> getTimeStringFromTimestamp(day.realBedtime) }

    // LiveData for real TWT
    val realTWTString =
        Transformations.map(mLiveSleepDay) { day -> getStringForRealTWT(day.realTWT) }

    init {
        Log.i("DayViewModel", "DayViewModel is created.")

        initializeDay()
    }

    private fun initializeDay() {
        viewModelScope.launch {
            val result = getDayFromDatabase()
            if (result != null) {
                mDay = result
                _mLiveSleepDay.value = mDay
            }
        }
    }

    private suspend fun getDayFromDatabase(): SleepDay? {
        return withContext(Dispatchers.IO) {
            database.getLastDay()
        }
    }

    // Adds another nap to the SleepDay object.
    fun addNap() {
        if (mDay.naps.size < MAX_NAPS_NUMBER) {
            mDay.naps.add(Nap(napDate = mDay.date))
            _mLiveSleepDay.value = mDay
            saveData()
        }
    }

    // Deletes the nap with index from a view.
    fun deleteNap(index: Int) {
        mDay.naps.removeAt(index)
        _mLiveSleepDay.value = mDay
        saveData()

    }

    // Clear realBedtime after end icon click.
    fun clearBedtime() {
        mDay.realBedtime = 0
        _mLiveSleepDay.value = mDay
        saveData()
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
        saveData()
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

    fun saveData() {
        viewModelScope.launch {
            saveDay()
        }
    }

    private suspend fun saveDay() {
        withContext(Dispatchers.IO) {
            database.insertSleepDay(mDay)
        }
    }
}