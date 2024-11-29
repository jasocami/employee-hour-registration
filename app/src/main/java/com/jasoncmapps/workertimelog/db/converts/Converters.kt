package com.jasoncmapps.workertimelog.db.converts

import androidx.room.TypeConverter
import java.util.*

/*
 * Created by jasoncastillejos on 2019-09-18.
 */

// https://developer.android.com/training/data-storage/room/referencing-data#kotlin

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}