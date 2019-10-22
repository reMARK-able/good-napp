package com.gmail.remarkable.development.goodnapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gmail.remarkable.development.goodnapp.SleepDay.Nap
import com.gmail.remarkable.development.goodnapp.database.SleepDatabase
import com.gmail.remarkable.development.goodnapp.database.SleepDatabaseDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Simple test for database.
 */

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        sleepDao = db.sleepDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetDay() {
        val day = SleepDay()
        sleepDao.insertSleepDay(day)
        val lastDay = sleepDao.getLastDay()
        assertEquals(lastDay?.date, "unknown_date")
    }

    @Test
    @Throws(Exception::class)
    fun insertSleepDayAndGetNapsWithIt() {
        val day = SleepDay()
        val nap1 = Nap(napDate = day.date, start = 435L, end = 786L)
        val nap2 = Nap(napDate = day.date, start = 987L, end = 244L)
        day.naps.add(nap1)
        day.naps.add(nap2)
        sleepDao.insertSleepDay(day)
        val lastDay = sleepDao.getLastDay()
        assertEquals(lastDay?.naps?.get(1)?.start, 987L)
        assertEquals(lastDay?.naps?.size, 2)
    }
}

