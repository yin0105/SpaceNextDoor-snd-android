package com.spacenextdoor.ui.activities

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class EventDecoratorColor(red: Int, dates: CalendarDay) :
    DayViewDecorator {

    var color: Int = red
    var date: CalendarDay = dates

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return date.toString() == day.toString()
    }

    override fun decorate(view: DayViewFacade?) {
        view!!.addSpan( ForegroundColorSpan(color))
    }


}