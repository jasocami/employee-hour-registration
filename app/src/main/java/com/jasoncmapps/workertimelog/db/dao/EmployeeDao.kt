package com.jasoncmapps.workertimelog.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jasoncmapps.workertimelog.db.entity.EmployeeItem

/*
 * Created by jasoncastillejos on 2019-09-21.
 */

@Dao
public interface EmployeeDao {

    @Query("SELECT * from employee ORDER BY name ASC")
    fun allEmployee(): LiveData<List<EmployeeItem>>

    @Query("SELECT * from employee WHERE id=:id ORDER BY name ASC")
    fun getEmployee(id: Int): EmployeeItem

    @Insert //(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(employeeItem: EmployeeItem)

    @Update //(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(employeeItem: EmployeeItem)

    @Delete
    suspend fun delete(employeeItem: EmployeeItem)

}