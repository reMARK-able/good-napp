package com.gmail.remarkable.development.goodnapp.util

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class TodayDecorator(val myDay: CalendarDay) : DayViewDecorator {


    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return myDay == day
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5f, Color.GREEN))
    }
}