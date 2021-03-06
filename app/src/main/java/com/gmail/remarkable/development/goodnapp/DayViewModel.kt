package com.gmail.remarkable.development.goodnapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.gmail.remarkable.development.goodnapp.SleepDay.Nap
import com.gmail.remarkable.development.goodnapp.database.SleepDatabaseDao
import com.gmail.remarkable.development.goodnapp.util.*
import kotlinx.coroutines.*

const val MAX_NAPS_NUMBER = 5
const val MAX_AWAKES_NUMBER = 5

class DayViewModel(
    val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val days = database.getAllDays()
    val daysInPairs = days.switchMap {
        liveData {
            var pairs = listOf<Pair<SleepDay, Long?>>()
            coroutineScope {
                launch { pairs = makePairs(it) }
                val exitAnimTime =
                    resources.getInteger(R.integer.day_fragment_exit_duration).toLong()
                delay(exitAnimTime)
            }
            emit(pairs)
        }
    }

    val sleepCalendarDays = days.switchMap {
        liveData {
            val days = getCalendarDays(it)
            emit(days)
        }
    }


    private val _mLiveSleepDay = MutableLiveData<SleepDay>()
    val mLiveSleepDay: LiveData<SleepDay>
        get() = _mLiveSleepDay

    val nextDayWakeUp = mLiveSleepDay.switchMap {
        liveData {
            val nextDay = getDayFromDatabase(it.date.nextDay())
            emit(getValidNextDayWakeUp(nextDay, resources))
        }
    }

    // LiveData for navigation to ConfirmationFragment.
    private val _navigateToConfirmation = MutableLiveData<ConfirmActions>()
    val navigateToConfirmation: LiveData<ConfirmActions>
        get() = _navigateToConfirmation

    // LiveData for navigation after fab clicked event.
    private val _navigateToToday = MutableLiveData<Boolean>()
    val navigateToToday: LiveData<Boolean>
        get() = _navigateToToday

    var mDay = SleepDay()

    private val resources = application.resources

    // LiveData to set Add nap button enabled or disabled.
    val isAllDataValid = Transformations.map(mLiveSleepDay) { day ->
        if (day.naps.size >= MAX_AWAKES_NUMBER) false
        else validateData(day, resources)
    }

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

    // LiveData for date text field.
    val dateString =
        Transformations.map(mLiveSleepDay) { day -> getDateString(day.date, application) }

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
        getDurationNonEmptyString(
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

    init {
        Log.i("DayViewModel", "DayViewModel is created.")
        //fillDatabase()
        //initializeDay()
    }

//    /**
//     * Initialize data for DayFragment.
//     * Gets data from database or if there is no data yet,
//     * sets new empty SleepDay object for LiveData holder.
//     */
//    private fun initializeDay() {
//        viewModelScope.launch {
//            val result = getDayFromDatabase()
//            if (result != null) {
//                mDay = result
//            } else {
//                mDay.date = getTodayInMillis()
//            }
//            _mLiveSleepDay.value = mDay
//        }
//    }

    /**
     * The suspend method to get data in background thread from database,
     * for coroutine started in initializeDay().
     */
    private suspend fun getDayFromDatabase(date: Long): SleepDay? {
        return withContext(Dispatchers.IO) {
            database.get(date)
        }
    }

    fun onFabClicked() {
        _navigateToToday.value = true
    }

    // Invoke when item in ListDayFragment is clicked.
    fun onNavigateToDay(date: Long) {
        viewModelScope.launch {
            val day = getDayFromDatabase(date) ?: SleepDay().also { it.date = date }
            mDay = day
            _mLiveSleepDay.value = mDay
            _navigateToToday.value = false
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

    fun confirmDeleteNap(index: Int) {
        navigateToConfirmationDialog(ConfirmActions.DeleteNap(index))
    }

    fun confirmDeleteAwake(index: Int) {
        navigateToConfirmationDialog(ConfirmActions.DeleteAwake(index))
    }

    fun confirmClear() {
        navigateToConfirmationDialog(ConfirmActions.ClearAll)
    }

    fun confirmDeleteDay() {
        navigateToConfirmationDialog(ConfirmActions.DeleteDay)
    }

    fun navigateToConfirmationDialog(action: ConfirmActions) {
        _navigateToConfirmation.value = action
    }

    fun clearDay() {
        val date = mDay.date
        mDay = SleepDay().also { it.date = date }
        _mLiveSleepDay.value = mDay
        saveData()
    }

    fun deleteDay() {
        viewModelScope.launch {
            deleteDayFromDatabase(mDay)
        }
    }

    // Deletes the nap with index from a view.
    fun deleteNap(index: Int) {
        mDay.naps.removeAt(index)
        _mLiveSleepDay.value = mDay
        saveData()
    }

    fun onCompleteNavigationToConfirmDialog() {
        _navigateToConfirmation.value = null
    }

    // Adds another nightAwake to the SleepDay.
    fun addAwake() {
        if (mDay.nightAwakes.size < MAX_AWAKES_NUMBER) {
            mDay.nightAwakes.add(SleepDay.NightAwake(nightAwakeDate = mDay.date))
            _mLiveSleepDay.value = mDay
            saveData()
        }
    }

    fun deleteAwake(index: Int) {
        mDay.nightAwakes.removeAt(index)
        _mLiveSleepDay.value = mDay
        saveData()
    }

    // Clear realBedtime field after end icon click.
    fun clearBedtime() {
        mDay.realBedtime = 0
        _mLiveSleepDay.value = mDay
        saveData()
    }

    fun saveComment(comment: String) {
        mDay.comment = comment
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

            AWAKE_1_START -> mDay.nightAwakes.getOrNull(0)?.start = timestamp
            AWAKE_1_END -> mDay.nightAwakes.getOrNull(0)?.end = timestamp
            AWAKE_2_START -> mDay.nightAwakes.getOrNull(1)?.start = timestamp
            AWAKE_2_END -> mDay.nightAwakes.getOrNull(1)?.end = timestamp
            AWAKE_3_START -> mDay.nightAwakes.getOrNull(2)?.start = timestamp
            AWAKE_3_END -> mDay.nightAwakes.getOrNull(2)?.end = timestamp
            AWAKE_4_START -> mDay.nightAwakes.getOrNull(3)?.start = timestamp
            AWAKE_4_END -> mDay.nightAwakes.getOrNull(3)?.end = timestamp
            AWAKE_5_START -> mDay.nightAwakes.getOrNull(4)?.start = timestamp
            AWAKE_5_END -> mDay.nightAwakes.getOrNull(4)?.end = timestamp
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

    private suspend fun deleteDayFromDatabase(day: SleepDay) {
        withContext(Dispatchers.IO) {
            database.deleteDay(day)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("DayViewModel", "DayViewModel is destroyed.")
    }
}