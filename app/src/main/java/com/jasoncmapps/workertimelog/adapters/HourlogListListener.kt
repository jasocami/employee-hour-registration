package com.jasoncmapps.workertimelog.adapters

import com.jasoncmapps.workertimelog.db.entity.HourlogItem

/*
 * Created by jasoncastillejos on 2019-09-22.
 */

interface HourlogListListener
{
    fun onHourlogClick(hourlogItem: HourlogItem)
}