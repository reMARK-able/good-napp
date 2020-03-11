package com.gmail.remarkable.development.goodnapp.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.gmail.remarkable.development.goodnapp.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class TodayDecorator(val context: Context) : DayViewDecorator {

    private val myDay = getTodayInMillis().toCalendarDay()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return myDay == day
    }

    override fun decorate(view: DayViewFacade?) {
        val circle = ContextCompat.getDrawable(context, R.drawable.circle)
        if (circle != null) view?.setBackgroundDrawable(circle)
    }
}

class SleepDaysDecorator(var sleepDays: Set<CalendarDay>, context: Context) : DayViewDecorator {

    val color = ContextCompat.getColor(context, R.color.colorPrimary)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day in sleepDays
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5f, color))
    }
}