package com.gmail.remarkable.development.goodnapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gmail.remarkable.development.goodnapp.SleepDay

@Dao
interface SleepDatabaseDao {

    @Insert
    fun insert(sleepDay: SleepTable)

    @Insert
    fun insertNaps(naps: List<SleepDay.Nap>)

    @Update
    fun update(sleepDay: SleepTable)

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

    @Transaction
    fun insertSleepDay(sleepDay: SleepDay) {
        val dayToInsert = SleepTable(
            sleepDay.date,
            sleepDay.targetTWT,
            sleepDay.wakeUp,
            sleepDay.outOfBed,
            sleepDay.realBedtime
        )
        insert(dayToInsert)
        val napsToInsert = sleepDay.naps
        insertNaps(napsToInsert)
    }
}