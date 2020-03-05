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
import com.gmail.remarkable.development.goodnapp.util.getMillisFromCalendarDay
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

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
                val millisTime = getMillisFromCalendarDay(date)
                Log.i(
                    "CalendarFragment",
                    "day=${date.day}; month=${date.month}; year=${date.year}; millis= $millisTime"
                )
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
