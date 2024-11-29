package com.jasoncmapps.workertimelog.extensions

import android.content.Context
import android.content.Intent
import com.jasoncmapps.workertimelog.ActivityLogList
import com.jasoncmapps.workertimelog.db.entity.EmployeeItem

/*
 * Created by jasoncastillejos on 2019-09-22.
 */

fun Context.buildActivityHourLogList(employeeItem: EmployeeItem): Intent
{
    return Intent(this, ActivityLogList::class.java).apply {
        putExtra(ActivityLogList.ARG_EMPLOYEE_OBJ, employeeItem)
    }
}
