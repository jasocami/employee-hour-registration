package com.jasoncmapps.workertimelog.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/*
 * Created by jasoncastillejos on 2019-09-18.
 */

@Entity(tableName = "employee")
@Parcelize
data class EmployeeItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "is_active")
    var isActive: Boolean
) : Parcelable {


}