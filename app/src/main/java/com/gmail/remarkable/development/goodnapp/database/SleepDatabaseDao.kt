package com.gmail.remarkable.development.goodnapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gmail.remarkable.development.goodnapp.SleepDay

@Dao
interface SleepDatabaseDao {

    @Insert
    fun insert(sleepDay: SleepDay)

    @Update
    fun update(sleepDay: SleepDay)

    // Deletes all from the table
    @Query("DELETE FROM sleep_table")
    fun clear()

    @Query("SELECT * FROM sleep_table ORDER BY date DESC")
    fun getAllDays(): LiveData<List<SleepDay>>

    @Query("SELECT * FROM sleep_table ORDER BY date DESC LIMIT 1")
    fun getLastDay(): SleepDay

    @Transaction
    @Query("SELECT * FROM sleep_table WHERE date = :date")
    fun get(date: String): SleepDay?
}