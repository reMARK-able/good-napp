package com.gmail.remarkable.development.goodnapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gmail.remarkable.development.goodnapp.SleepDay.Nap
import com.gmail.remarkable.development.goodnapp.SleepDay.NightAwake

@Database(
    entities = [SleepTable::class, Nap::class, NightAwake::class],
    version = 3,
    exportSchema = true
)
abstract class SleepDatabase : RoomDatabase() {

    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object {

        fun create(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            SleepDatabase::class.java,
            "sleep_history_database"
        )
            //.createFromAsset("sleep_history_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}