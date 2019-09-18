package com.gmail.remarkable.development.goodnapp


import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.util.*

/**
 * A TimePicker DialogFragment for data input.
 */
class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        val mDialog =
            TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))

        // This adds an optional slide up animation (remove this line for default fade_in animation).
        mDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSlideUp

        return mDialog
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Temporary - only to see the result
        val navHostFrag = activity!!.supportFragmentManager.findFragmentById(R.id.navHostFragment)
        navHostFrag?.view?.findViewById<TextInputEditText>(R.id.target_TWT_editText)
            ?.setText("$hourOfDay:$minute")
    }
}
