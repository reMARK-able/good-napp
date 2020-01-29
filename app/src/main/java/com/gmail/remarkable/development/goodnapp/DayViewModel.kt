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
        Transformations.map(mLiveSleepDay) { day ->
            getTimeStringFromTimestamp(
                day.outOfBed,
                application
            )
        }

    // LiveData for targetTWT field.
    val targetTWTString =
        Transformations.map(mLiveSleepDay) { day -> getDurationString(day.targetTWT, resources) }

    // LiveData for outOfBed field.
    val wakeUpString =
        Transformations.map(mLiveSleepDay) { day ->
            getTimeStringFromTimestamp(
                day.wakeUp,
                application
            )
        }

    // LiveData for nap duration field.
    fun napDurationString(index: Int) = Transformations.map(mLiveSleepDay) { day ->
        getDurationNapString(
            day.naps.getOrNull(index)?.duration ?: 0,
            resources
        )
    }

    // LiveData for nap start field.
    fun napStartString(index: Int) = Transformations.map(mLiveSleepDay) { day ->
        getTimeStringFromTimestamp(
            day.naps.getOrNull(index)?.start ?: 0, getApplication()
        )
    }

    // LiveData for nap end field.
    fun napEndString(index: Int) = Transformations.map(mLiveSleepDay) { day ->
        getTimeStringFromTimestamp(
            day.naps.getOrNull(index)?.end ?: 0, getApplication()
        )
    }

    // LiveData for awake time fields.
    fun awakeTimeString(duration: Long) = Transformations.map(mLiveSleepDay) {
        getDurationString(duration, resources)
    }

    // LiveData for target bedtime.
    val targetBedtimeString =
        Transformations.map(mLiveSleepDay) { day ->
            getStringForTargetBedtime(
                day.targetBedtime,
                application
            )
        }

    // LiveData for real bedtime field.
    val realBedtimeString =
        Transformations.map(mLiveSleepDay) { day ->
            getTimeStringFromTimestamp(
                day.realBedtime,
                application
            )
        }

    // LiveData for real TWT
    val realTWTString =
        Transformations.map(mLiveSleepDay) { day -> getStringForRealTWT(day.realTWT, resources) }

    init {
        Log.i("DayViewModel", "DayViewModel is created.")

        initializeDay()
    }

    /**
     * Initialize data for DayFragment.
     * Gets data from database or if there is no data yet,
     * sets new empty SleepDay object for LiveData holder.
     */
    private fun initializeDay() {
        viewModelScope.launch {
            val result = getDayFromDatabase()
            if (result != null) {
                mDay = result
            }
            _mLiveSleepDay.value = mDay
        }
    }

    /**
     * The suspend method to get data in background thread from database,
     * for coroutine started in initializeDay().
     */
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

    // Clear realBedtime field after end icon click.
    fun clearBedtime() {
        mDay.realBedtime = 0
        _mLiveSleepDay.value = mDay
        saveData()
    }

    /**
     * Called when return from TimePickerDialogFragment.
     * Set a appropriate field in mDay object, pass it to the LiveData and write to database.
     */
    fun onTimeSet(viewId: String, hour: Int, minutes: Int, timestamp: Long) {

        when (viewId) {

            TARGET_TWT -> mDay.targetTWT = getDurationFromPicker(hour, minutes)
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
        val currentDuration = getDurationFromPicker(hour, minutes)
        Log.i("DayViewModel", "timestamp== $timestamp")
        Log.i("DayViewModel", "getDurationFromPicker== $currentDuration")
        Log.i(
            "DayViewModel",
            "getDurationString== ${getDurationString(currentDuration, resources)}"
        )

    }

    /**
     * Starts coroutine in which the saveDay() is called for writing to database.
     */
    fun saveData() {
        viewModelScope.launch {
            saveDay()
        }
    }

    // Background thread suspend method to write SleepDay object in the database.
    private suspend fun saveDay() {
        withContext(Dispatchers.IO) {
            database.insertSleepDay(mDay)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("DayViewModel", "DayViewModel is destroyed.")
    }
}