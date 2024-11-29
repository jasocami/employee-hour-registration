package com.jasoncmapps.workertimelog.adapters

import com.jasoncmapps.workertimelog.db.entity.EmployeeItem

/*
 * Created by jasoncastillejos on 2019-09-22.
 */

interface EmployeeListListener
{

    fun onEmployeeClick(employee: EmployeeItem)

}