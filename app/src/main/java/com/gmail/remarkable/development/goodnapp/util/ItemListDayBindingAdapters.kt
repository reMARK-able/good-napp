package com.gmail.remarkable.development.goodnapp.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.gmail.remarkable.development.goodnapp.R
import com.gmail.remarkable.development.goodnapp.SleepDay


@BindingAdapter("awakeTimesFormatted")
fun TextView.setAwakeTimesFormatted(sleepDay: SleepDay?) {
    text = when (sleepDay) {
        null -> "n/a"
        else -> getAllAwakeTimesString(sleepDay.awakeTimes, context.resources)
    }
}

@BindingAdapter("timelineBottom")
fun ImageView.setTimelineBottom(sleepDay: SleepDay?) {
    val isWakeUpValid = sleepDay?.wakeUp != 0L && validWakeUp(sleepDay) == null
    val bottomDrawableId = if (isWakeUpValid) R.drawable.timeline_drawable_filled
    else R.drawable.timeline_drawable_bottom_gradient
    val bottomDrawable = ContextCompat.getDrawable(context, bottomDrawableId)

    setImageDrawable(bottomDrawable)
}

@BindingAdapter("timelineTop")
fun ImageView.setTimelineTop(sleepDay: SleepDay?) {
    val isRealBedTimeValid =
        sleepDay?.realBedtime != 0L && validRealBedtime(sleepDay, context.resources) == null
    val topDrawableId = if (isRealBedTimeValid) R.drawable.timeline_drawable_filled
    else R.drawable.timeline_drawable_top_gradient
    val topDrawable = ContextCompat.getDrawable(context, topDrawableId)

    setImageDrawable(topDrawable)
}