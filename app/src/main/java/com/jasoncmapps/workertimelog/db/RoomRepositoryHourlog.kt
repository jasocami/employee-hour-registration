package com.jasoncmapps.workertimelog.db

import androidx.lifecycle.LiveData
import com.jasoncmapps.workertimelog.db.dao.EmployeeDao
import com.jasoncmapps.workertimelog.db.dao.HourlogDao
import com.jasoncmapps.workertimelog.db.entity.EmployeeItem
import com.jasoncmapps.workertimelog.db.entity.HourlogItem
import java.util.*

/*
 * Created by jasoncastillejos on 2019-09-21.
 */

public class RoomRepositoryHourlog(private val hourlogDao: HourlogDao) {

    suspend fun insert(hourlogItem: HourlogItem){
        hourlogDao.insert(hourlogItem)
    }

    fun getHourlog(id: Int): HourlogItem {
        return hourlogDao.getLog(id)
    }

    suspend fun updateHourlog(hourlogItem: HourlogItem) {
        return hourlogDao.update(hourlogItem)
    }

    suspend fun deleteHourlog(hourlogItem: HourlogItem) {
        hourlogDao.delete(hourlogItem)
    }

    suspend fun deleteAllHourlog(employeeId: Int) {
        hourlogDao.deleteAll(employeeId)
    }

    fun getAllHourlog(): List<HourlogItem> {
        return hourlogDao.getAllLogs()
    }

    fun allHourlog(): LiveData<List<HourlogItem>> {
        return hourlogDao.allLogs()
    }

    fun allHourlog(employeeId: Int): LiveData<List<HourlogItem>> {
        return hourlogDao.allLogs(employeeId)
    }

    fun allHourlog(employeeId: Int, from:Date, to:Date): LiveData<List<HourlogItem>> {
        return hourlogDao.allLogs(employeeId, from, to)
    }

    suspend fun getAllHourlog(employeeId: Int): List<HourlogItem> {
        return hourlogDao.getAllLogs(employeeId)
    }
    suspend fun getAllHourlog(employeeId: Int, from: Date, to: Date): List<HourlogItem> {
        return hourlogDao.getAllLogs(employeeId, from, to)
    }

}