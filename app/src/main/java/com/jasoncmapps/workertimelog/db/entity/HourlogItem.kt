package com.jasoncmapps.workertimelog.db.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*

/*
 * Created by jasoncastillejos on 2019-09-18.
 */

@Entity(tableName = "hourlog", indices = [Index("id")],
    foreignKeys = [
        ForeignKey(entity = EmployeeItem::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("employee_id"),
            onDelete = ForeignKey.CASCADE)
    ]
)
@Parcelize
data class HourlogItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "timestamp")
    var timestamp: Date,
    @ColumnInfo(name = "employee_id")
    val employee_id: Int
) : Parcelable