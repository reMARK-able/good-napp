package com.gmail.remarkable.development.goodnapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gmail.remarkable.development.goodnapp.databinding.FragmentListDaysBinding
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*

/**
 * Fragment to display a list of SleepDays.
 */
class ListDaysFragment : Fragment() {

    val fakeData = setFakeData()

    private fun setFakeData(): List<SleepDay> {
        val calendarUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendarUTC.timeInMillis = 1577836800000
        val dateList = MutableList(31) {
            calendarUTC.add(Calendar.DATE, 1)
            calendarUTC.timeInMillis
        }
        val fakeDaysList = mutableListOf<SleepDay>()
        for (date in dateList) {
            val newDay = SleepDay(mutableListOf())
            newDay.date = date
            fakeDaysList.add(newDay)
        }
        return fakeDaysList.reversed()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListDaysBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_days, container, false)

        val viewModel: DayViewModel by inject { parametersOf(this) }

        val adapter = SleepDayAdapter()
        adapter.data = fakeData
        binding.dayList.adapter = adapter

        return binding.root
    }


}
