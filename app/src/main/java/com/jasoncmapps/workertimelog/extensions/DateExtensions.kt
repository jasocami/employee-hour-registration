package com.jasoncmapps.workertimelog.extensions

import java.text.SimpleDateFormat
import java.util.*

/*
 * Created by jasoncastillejos on 2019-09-23.
 */

fun Date.startOfDay() : Date {
    val instance = Calendar.getInstance()
    instance.time = this
    instance.set(Calendar.HOUR_OF_DAY, 0)
    instance.set(Calendar.MINUTE, 0)
    instance.set(Calendar.SECOND, 0)
    instance.set(Calendar.MILLISECOND, 0)
    return instance.time
}

fun Date.endOfDay() : Date {
    val instance = Calendar.getInstance()
    instance.time = this
    instance.set(Calendar.HOUR_OF_DAY, 23)
    instance.set(Calendar.MINUTE, 59)
    instance.set(Calendar.SECOND, 59)
    instance.set(Calendar.MILLISECOND, 0)
    return instance.time
}

fun Date.isToday(): Boolean {
    val obj = Calendar.getInstance()
    obj.time = this
    val now = Calendar.getInstance()

    return obj.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH) &&
        obj.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
        obj.get(Calendar.YEAR) == now.get(Calendar.YEAR)
}

fun SimpleDateFormat.withGMT (): SimpleDateFormat {
    this.timeZone = TimeZone.getTimeZone("GMT")
    return this
}

fun SimpleDateFormat.withUTC (): SimpleDateFormat {
    this.timeZone = TimeZone.getTimeZone("UTC")
    return this
}