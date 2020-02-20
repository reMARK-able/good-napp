package com.gmail.remarkable.development.goodnapp


import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.gmail.remarkable.development.goodnapp.util.getTodayInMillis
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A TimePicker DialogFragment for data input.
 */
class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val viewModel: DayViewModel by inject { parametersOf(this) }

    val args: TimePickerDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val hour = getCurrentHour()
        val minute = getCurrentMin()

        // If the input field is TARGET_TWT then Picker is set to 24H else it checks user phone settings.
        // Setting time duration in format AM/PM would be illogical.
        val is24Format =
            if (args.viewNameTag == TARGET_TWT) true else DateFormat.is24HourFormat(activity)
        // Create a new instance of TimePickerDialog and return it
        val mDialog =
            TimePickerDialog(activity, this, hour, minute, is24Format)

        // This adds an optional slide up animation (remove this line for default fade_in animation).
        mDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSlideUp

        return mDialog
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

        val day = getCurrentDay()
        val viewId = args.viewNameTag
        val timestamp = getTimeStamp(day, hourOfDay, minute)
        Log.i("TimePickerDialog", "Picked time in millis: $timestamp")
        viewModel.onTimeSet(viewId, hourOfDay, minute, timestamp)
    }

    private fun getTimeStamp(date: Long, hour: Int, min: Int): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = date
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar.set(Calendar.SECOND, 0) // Must be set for the same timestamp on every click.
        calendar.set(Calendar.MILLISECOND, 0) // Must be set for the same timestamp on every click.

        return calendar.timeInMillis
    }

    /**
     * Get actual date from SleepDay to properly set the picker's initial values.
     */
    private fun getCurrentDay(): Long {
        val currentDate = viewModel.mDay.date
        return if (viewModel.mDay.date != 0L) currentDate else getTodayInMillis()
    }

    /**
     * Gets the hour previously set on the field or if it's empty sets the actual time.
     * (for TARGET_TWT field it set time duration to 12 hr 0 min - if it's empty.
     */
    private fun getCurrentHour(): Int {
        val calendar = Calendar.getInstance()
        val calendarUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val viewName = args.viewNameTag
        return when (viewName) {
            TARGET_TWT -> {
                val dataFromDay = getDataFromDay(viewName)
                if (dataFromDay != 0L) TimeUnit.MILLISECONDS.toHours(dataFromDay).toInt() else 12
            }
            else -> {
                val dataFromDay = getDataFromDay(viewName)
                if (dataFromDay != 0L) {
                    calendarUTC.timeInMillis = getDataFromDay(viewName)
                    calendarUTC.get(Calendar.HOUR_OF_DAY)
                } else calendar.get(Calendar.HOUR_OF_DAY)
            }
        }
    }

    /**
     * Gets minutes previously set on the field or if it's empty sets the actual time.
     * (for TARGET_TWT field it set time duration to 12 hr 0 min - if it's empty.
     */
    private fun getCurrentMin(): Int {
        val calendar = Calendar.getInstance()
        val calendarUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val viewName = args.viewNameTag
        return when (viewName) {
            TARGET_TWT -> {
                val dataFromDay = getDataFromDay(viewName)
                if (dataFromDay != 0L) {
                    (dataFromDay / (1000 * 60) % 60).toInt()
                } else 0
            }
            else -> {
                val dataFromDay = getDataFromDay(viewName)
                if (dataFromDay != 0L) {
                    calendarUTC.timeInMillis = dataFromDay
                    calendarUTC.get(Calendar.MINUTE)
                } else calendar.get(Calendar.MINUTE)
            }
        }
    }

    /**
     * Gets actual data from Day() object to set the picker's initial value.
     */
    private fun getDataFromDay(viewName: String): Long {
        val mDay = viewModel.mDay
        return when (viewName) {
            TARGET_TWT -> mDay.targetTWT
            WAKE_UP -> mDay.wakeUp
            OUT_OF_BED -> mDay.outOfBed
            REAL_BEDTIME -> mDay.realBedtime
            NAP_1_START -> mDay.naps[0].start
            NAP_1_END -> mDay.naps[0].end
            NAP_2_START -> mDay.naps[1].start
            NAP_2_END -> mDay.naps[1].end
            NAP_3_START -> mDay.naps[2].start
            NAP_3_END -> mDay.naps[2].end
            NAP_4_START -> mDay.naps[3].start
            NAP_4_END -> mDay.naps[3].end
            NAP_5_START -> mDay.naps[4].start
            NAP_5_END -> mDay.naps[4].end
            AWAKE_1_START -> mDay.nightAwakes.getOrNull(0)?.start ?: 0
            AWAKE_1_END -> mDay.nightAwakes.getOrNull(0)?.end ?: 0
            AWAKE_2_START -> mDay.nightAwakes.getOrNull(1)?.start ?: 0
            AWAKE_2_END -> mDay.nightAwakes.getOrNull(1)?.end ?: 0
            AWAKE_3_START -> mDay.nightAwakes.getOrNull(2)?.start ?: 0
            AWAKE_3_END -> mDay.nightAwakes.getOrNull(2)?.end ?: 0
            AWAKE_4_START -> mDay.nightAwakes.getOrNull(3)?.start ?: 0
            AWAKE_4_END -> mDay.nightAwakes.getOrNull(3)?.end ?: 0
            AWAKE_5_START -> mDay.nightAwakes.getOrNull(4)?.start ?: 0
            AWAKE_5_END -> mDay.nightAwakes.getOrNull(4)?.end ?: 0
            else -> throw IllegalArgumentException("Unknown view name")
        }
    }
}
