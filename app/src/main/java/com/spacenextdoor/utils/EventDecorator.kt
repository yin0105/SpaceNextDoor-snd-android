package com.spacenextdoor.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.spacenextdoor.ui.activities.UnitDetailsActivity


class EventDecorator(red: Int, dates: CalendarDay) :
    DayViewDecorator {

    var color: Int = red
    var date: CalendarDay = dates

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return date.toString() == day.toString()
    }

    override fun decorate(view: DayViewFacade?) {
        view!!.addSpan(DotSpan(15.0f, color))
    }
}