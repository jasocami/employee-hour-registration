package com.jasoncmapps.workertimelog.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jasoncmapps.workertimelog.db.entity.HourlogItem
import java.util.*

/*
 * Created by jasoncastillejos on 2019-09-21.
 */

@Dao
public interface HourlogDao {

    @Query("SELECT * from hourlog ORDER BY timestamp DESC")
    fun allLogs(): LiveData<List<HourlogItem>>

    @Query("SELECT * from hourlog WHERE employee_id=:employeeId ORDER BY timestamp DESC")
    fun allLogs(employeeId: Int): LiveData<List<HourlogItem>>

    @Query("SELECT * from hourlog WHERE employee_id=:employeeId AND timestamp BETWEEN :from AND :to ORDER BY timestamp DESC")
    fun allLogs(employeeId: Int, from:Date, to:Date): LiveData<List<HourlogItem>>

    @Query("SELECT * from hourlog WHERE id=:id")
    fun getLog(id: Int): HourlogItem

    @Query("SELECT * from hourlog ORDER BY timestamp DESC")
    fun getAllLogs(): List<HourlogItem>

    @Query("SELECT * from hourlog WHERE employee_id=:employeeId ORDER BY timestamp DESC")
    suspend fun getAllLogs(employeeId: Int): List<HourlogItem>

    @Query("SELECT * from hourlog WHERE employee_id=:employeeId AND timestamp BETWEEN :from AND :to ORDER BY timestamp DESC")
    suspend fun getAllLogs(employeeId: Int, from: Date, to: Date): List<HourlogItem>

    @Insert //(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(hourlog: HourlogItem)

    @Update //(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(hourlog: HourlogItem)

    @Delete //(onConflict = OnConflictStrategy.IGNORE)
    suspend fun delete(hourlog: HourlogItem)

    @Query("DELETE FROM hourlog WHERE employee_id=:employeeId")
    suspend fun deleteAll(employeeId: Int)

}