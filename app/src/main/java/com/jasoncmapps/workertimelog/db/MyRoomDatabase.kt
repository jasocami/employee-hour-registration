package com.jasoncmapps.workertimelog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jasoncmapps.workertimelog.db.converts.Converters
import com.jasoncmapps.workertimelog.db.dao.EmployeeDao
import com.jasoncmapps.workertimelog.db.dao.HourlogDao
import com.jasoncmapps.workertimelog.db.entity.EmployeeItem
import com.jasoncmapps.workertimelog.db.entity.HourlogItem
import kotlinx.coroutines.CoroutineScope

/*
 * Created by jasoncastillejos on 2019-09-21.
 */

@Database(entities = [EmployeeItem::class, HourlogItem::class], version = 1)
@TypeConverters(Converters::class)
public abstract class MyRoomDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao
    abstract fun hourlogDao(): HourlogDao

    private class MyDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
          /*  INSTANCE?.let { database ->
                scope.launch {
                    // Do something on launch
                }
            }

           */
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: MyRoomDatabase? = null

        public fun getDatabase(context: Context,
                               scope: CoroutineScope): MyRoomDatabase
        {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyRoomDatabase::class.java,
                    "hourlog_database"
                )
                    .addCallback(
                        MyDatabaseCallback(
                            scope
                        )
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }

}