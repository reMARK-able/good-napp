package com.gmail.remarkable.development.goodnapp

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gmail.remarkable.development.goodnapp.databinding.FragmentCalendarBinding
import com.gmail.remarkable.development.goodnapp.util.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment() {

    lateinit var binding: FragmentCalendarBinding
    val viewModel: DayViewModel by inject { parametersOf(this) }

    val todayCalendarDay = getTodayInMillis().toCalendarDay()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val todayDecorator = TodayDecorator(requireContext())
        val sleepDaysDecorator = SleepDaysDecorator(emptySet(), requireContext())
        binding.calendarView.state().edit()
            .setMaximumDate(todayCalendarDay)
            .commit()
        binding.calendarView.addDecorators(todayDecorator, sleepDaysDecorator)

        viewModel.sleepCalendarDays.observe(viewLifecycleOwner, Observer { days ->
            sleepDaysDecorator.sleepDays = days
            binding.calendarView.invalidateDecorators()
        })
        binding.bottom.container.setOnClickListener {
            val date = viewModel.mLiveSleepDay.value?.date ?: 0
            navigateToDay(date)
        }

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                val millisTime = date.toMillisUTCDate()
                Log.i(
                    "CalendarFragment",
                    "day=${date.day}; month=${date.month}; year=${date.year}; millis= $millisTime"
                )
                viewModel.onNavigateToDay(millisTime)
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.calendar_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.goToToday -> {
                val today = getTodayInMillis()
                val calendarDayToday = today.toCalendarDay()
                viewModel.onNavigateToDay(today)
                binding.calendarView.selectedDate = calendarDayToday
                binding.calendarView.currentDate = calendarDayToday
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToDay(date: Long) {
        val dateString = getDateString(date, requireContext())
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.calendarFragment) {
            findNavController().navigate(
                CalendarFragmentDirections.actionCalendarFragmentToDayFragment(
                    dateString
                )
            )
        }
    }

}
