package com.gmail.remarkable.development.goodnapp


import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import java.util.*

/**
 * A TimePicker DialogFragment for data input.
 */
class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val viewModel: DayViewModel by navGraphViewModels(R.id.navigation)

    val args: TimePickerDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val currentValue = args.currentValue
        var hour = 0
        var minute = 0
        // Get time that was set previously.
        if (currentValue.isNotEmpty()) {
            hour = currentValue.substringBefore(':').toInt()
            minute = currentValue.substringAfter(':').toInt()
        } else {
            // Use the current time as the default values for the picker
            val c = Calendar.getInstance()
            hour = c.get(Calendar.HOUR_OF_DAY)
            minute = c.get(Calendar.MINUTE)
        }
        // Create a new instance of TimePickerDialog and return it
        val mDialog =
            TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))

        // This adds an optional slide up animation (remove this line for default fade_in animation).
        mDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSlideUp

        return mDialog
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {


        val viewId = args.viewNameTag
        val timestamp = getTimeStamp(hourOfDay, minute)
        Log.i("TimePickerDialog", "Picked time in millis: $timestamp")
        viewModel.onTimeSet(viewId, hourOfDay, minute, timestamp)
    }

    private fun getTimeStamp(hour: Int, min: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar.set(Calendar.SECOND, 0) // Must be set for the same timestamp on every click.
        calendar.set(Calendar.MILLISECOND, 0) // Must be set for the same timestamp on every click.

        return calendar.timeInMillis
    }
}
