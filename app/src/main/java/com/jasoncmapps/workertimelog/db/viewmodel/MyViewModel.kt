package com.jasoncmapps.workertimelog.db.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jasoncmapps.workertimelog.db.MyRoomDatabase
import com.jasoncmapps.workertimelog.db.RoomRepositoryEmployee
import com.jasoncmapps.workertimelog.db.RoomRepositoryHourlog
import com.jasoncmapps.workertimelog.db.entity.EmployeeItem
import com.jasoncmapps.workertimelog.db.entity.HourlogItem
import com.jasoncmapps.workertimelog.extensions.endOfDay
import com.jasoncmapps.workertimelog.extensions.startOfDay
import kotlinx.coroutines.launch
import java.util.*

/*
 * Created by jasoncastillejos on 2019-09-21.
 */

public class MyViewModel(application: Application): AndroidViewModel(application) {

    private val repositoryEmployee: RoomRepositoryEmployee
    private val repositoryHourlog: RoomRepositoryHourlog
    private val mDatabase: MyRoomDatabase = MyRoomDatabase.getDatabase(application, viewModelScope)

    init {
        val employeeDao = mDatabase.employeeDao()
        val hourlogDao = mDatabase.hourlogDao()
        repositoryEmployee = RoomRepositoryEmployee(employeeDao)
        repositoryHourlog = RoomRepositoryHourlog(hourlogDao)
    }

    /*
            Employee
     */

    fun allEmployee() = repositoryEmployee.allEmployees

    fun getEmployee(id: Int) = repositoryEmployee.getEmployee(id)

    fun insertEmployee(employee: EmployeeItem) = viewModelScope.launch {
        repositoryEmployee.insert(employee)
    }

    fun updateEmployee(employee: EmployeeItem) = viewModelScope.launch {
        repositoryEmployee.updateEmployee(employee)
    }

    fun deleteEmployee(employee: EmployeeItem) = viewModelScope.launch {
        repositoryEmployee.deleteEmployee(employee)
    }

    /*
            Hour log
     */

    fun allHourLog(employeeId: Int) = repositoryHourlog.allHourlog(employeeId)
    fun allHourLog(employeeId: Int, from: Date, to: Date) = repositoryHourlog.allHourlog(employeeId, from, to)

    suspend fun getAllHourLog(employeeId: Int): List<HourlogItem> {
        return repositoryHourlog.getAllHourlog(employeeId)
    }
    suspend fun getAllHourLog(employeeId: Int, from: Date, to: Date): List<HourlogItem> {
        return repositoryHourlog.getAllHourlog(employeeId, from, to)
    }

    fun updateStatusEmployee(employee: EmployeeItem, myViewModel: MyViewModel) = viewModelScope.launch {
        employee.isActive = getAllHourLog(employee.id,Date().startOfDay(), Date().endOfDay()).size % 2 != 0
        myViewModel.updateEmployee(employee)
    }

    fun insertLog(hourlog: HourlogItem) = viewModelScope.launch {
        repositoryHourlog.insert(hourlog)
    }

    fun updateHourLog(hourlog: HourlogItem) = viewModelScope.launch {
        repositoryHourlog.updateHourlog(hourlog)
    }

    fun deleteHourLog(hourlog: HourlogItem) = viewModelScope.launch {
        repositoryHourlog.deleteHourlog(hourlog)
    }

    fun deleteAllHourLog(employeeId: Int) = viewModelScope.launch {
        repositoryHourlog.deleteAllHourlog(employeeId)
    }

}