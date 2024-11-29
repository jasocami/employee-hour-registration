package com.jasoncmapps.workertimelog.db

import androidx.lifecycle.LiveData
import com.jasoncmapps.workertimelog.db.dao.EmployeeDao
import com.jasoncmapps.workertimelog.db.entity.EmployeeItem

/*
 * Created by jasoncastillejos on 2019-09-21.
 */

public class RoomRepositoryEmployee(private val employeeDao: EmployeeDao) {

    val allEmployees: LiveData<List<EmployeeItem>> = employeeDao.allEmployee()

    suspend fun insert(employee: EmployeeItem){
        employeeDao.insert(employee)
    }

    fun getEmployee(id: Int): EmployeeItem {
        return employeeDao.getEmployee(id)
    }

    suspend fun updateEmployee(employeeItem: EmployeeItem) {
        return employeeDao.update(employeeItem)
    }

    suspend fun deleteEmployee(employeeItem: EmployeeItem) {
        employeeDao.delete(employeeItem)
    }

}