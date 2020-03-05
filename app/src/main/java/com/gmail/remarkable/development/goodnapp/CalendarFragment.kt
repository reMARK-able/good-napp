package com.gmail.remarkable.development.goodnapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gmail.remarkable.development.goodnapp.databinding.FragmentCalendarBinding
import com.gmail.remarkable.development.goodnapp.util.getUTCCalendar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment() {

    lateinit var binding: FragmentCalendarBinding
    val viewModel: DayViewModel by inject { parametersOf(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                val calendarUTC = getUTCCalendar()
                val day = date.day
                val month = date.month
                val year = date.year
                calendarUTC.set(Calendar.YEAR, year)
                calendarUTC.set(Calendar.MONTH, month - 1)
                calendarUTC.set(Calendar.DAY_OF_MONTH, day)
                calendarUTC.set(Calendar.HOUR_OF_DAY, 0)
                calendarUTC.set(Calendar.MINUTE, 0)
                calendarUTC.set(Calendar.SECOND, 0)
                calendarUTC.set(Calendar.MILLISECOND, 0)
                val millisTime = calendarUTC.timeInMillis
                Log.i("CalenFrag", "day=$day; month=$month; year=$year; millis= $millisTime")
                navigateToDay(millisTime)
            }
        }

        return binding.root
    }

    private fun navigateToDay(date: Long) {
        viewModel.onNavigateToDay(date)
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.calendarFragment) {
            findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToDayFragment())
        }
    }

}
