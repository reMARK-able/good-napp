package com.gmail.remarkable.development.goodnapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gmail.remarkable.development.goodnapp.SleepDay

@Dao
interface SleepDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sleepDay: SleepTable)

    @Insert
    fun insertNaps(naps: List<SleepDay.Nap>)

    @Insert
    fun insertAwakes(awakes: List<SleepDay.NightAwake>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDaysWithoutNaps(days: List<SleepTable>)

    @Update
    fun update(sleepDay: SleepTable)

    // Deletes all from the table
    @Query("DELETE FROM sleep_table")
    fun clear()

    @Transaction
    @Query("SELECT * FROM sleep_table ORDER BY date DESC")
    fun getAllDays(): LiveData<List<SleepDay>>

    @Transaction
    @Query("SELECT * FROM sleep_table ORDER BY date DESC LIMIT 1")
    fun getLastDay(): SleepDay?

    @Transaction
    @Query("SELECT * FROM sleep_table WHERE date = :date")
    fun get(date: Long): SleepDay?

    @Transaction
    fun insertSleepDay(sleepDay: SleepDay) {
        val dayToInsert = SleepTable(
            sleepDay.date,
            sleepDay.targetTWT,
            sleepDay.wakeUp,
            sleepDay.outOfBed,
            sleepDay.realBedtime,
            sleepDay.comment
        )
        insert(dayToInsert)
        val napsToInsert = sleepDay.naps
        insertNaps(napsToInsert)
        val awakesToInsert = sleepDay.nightAwakes
        insertAwakes(awakesToInsert)
    }

    @Delete
    fun deleteSleepDay(
        day: SleepTable,
        naps: List<SleepDay.Nap>,
        nightAwakes: List<SleepDay.NightAwake>
    )

    @Transaction
    fun deleteDay(sleepDay: SleepDay) {
        val sleepTable = SleepTable(
            sleepDay.date,
            sleepDay.targetTWT,
            sleepDay.wakeUp,
            sleepDay.outOfBed,
            sleepDay.realBedtime,
            sleepDay.comment
        )
        val naps = sleepDay.naps
        val nightAwakes = sleepDay.nightAwakes
        deleteSleepDay(sleepTable, naps, nightAwakes)
    }
}