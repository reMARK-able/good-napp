package com.gmail.remarkable.development.goodnapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_table")
open class SleepTable(
    @PrimaryKey
    var date: Long = 0,

    var targetTWT: Long = 0,

    var wakeUp: Long = 0,

    var outOfBed: Long = 0,

    var realBedtime: Long = 0
)