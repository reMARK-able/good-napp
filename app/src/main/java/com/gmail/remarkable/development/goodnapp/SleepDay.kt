package com.gmail.remarkable.development.goodnapp

import androidx.room.*
import com.gmail.remarkable.development.goodnapp.database.SleepTable

data class SleepDay(
    @Relation(
        parentColumn = "date",
        entityColumn = "nap_date",
        entity = Nap::class
    )
    val naps: MutableList<Nap> = mutableListOf(),

    @Relation(
        parentColumn = "date",
        entityColumn = "night_awake_date",
        entity = NightAwake::class
    )
    val nightAwakes: MutableList<NightAwake> = mutableListOf()

) : SleepTable() {
    val targetBedtime: Long
        get() {
            if (outOfBed != 0L && targetTWT != 0L) return outOfBed + targetTWT + (naps.sumBy { (it.duration).toInt() })
            else return 0
        }

    val awakeTimes: List<Long>
        get() {
            return when {
                naps.isEmpty() -> {
                    if (realBedtime > outOfBed && outOfBed != 0L) listOf(realBedtime - outOfBed)
                    else listOf(0L)
                }
                naps.size == 1 -> {
                    val result = mutableListOf<Long>()
                    if (naps[0].start - outOfBed > 0 && outOfBed != 0L) {
                        result.add(naps[0].start - outOfBed)
                    } else result.add(0L)
                    if (realBedtime - naps[0].end > 0 && naps[0].end != 0L && naps[0].end > naps[0].start) {
                        result.add(realBedtime - naps[0].end)
                    } else result.add(0L)
//                    if (naps[0].start - outOfBed > 0 && realBedtime - naps[0].end > 0 && naps[0].end != 0L) {
//                        listOf(naps[0].start - outOfBed, realBedtime - naps[0].end)
//                    } else listOf(0L, 0L)
                    result
                }
                else -> {
                    val result = mutableListOf<Long>()
                    if (naps[0].start - outOfBed > 0 && outOfBed != 0L) {
                        result.add(naps[0].start - outOfBed)
                    } else result.add(0L)
                    for (i in 1 until naps.size) {
                        if (naps[i].start - naps[i - 1].end > 0 && naps[i - 1].end != 0L && naps[i - 1].end > naps[i - 1].start) {
                            val awakeTime = naps[i].start - naps[i - 1].end
                            result.add(awakeTime)
                        } else result.add(0L)
                    }
                    if (realBedtime - naps.last().end > 0 && naps.last().end != 0L && naps.last().end > naps.last().start) {
                        result.add(realBedtime - naps.last().end)
                    } else result.add(0L)

                    result
                }
            }
        }

    val realDayAwakeTime: Long
        get() {
            var sumOfAwakeTimes = 0L
            for (awakeTime in awakeTimes) {
                if (awakeTime != 0L) sumOfAwakeTimes += awakeTime else return 0L
                //if (awakeTimes.all { it > 0L }) return awakeTimes.sum()
            }
            return sumOfAwakeTimes
        }

    @Entity(
        tableName = "nap_table",
        foreignKeys = [ForeignKey(
            entity = SleepTable::class,
            parentColumns = ["date"],
            childColumns = ["nap_date"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )]
    )
    class Nap(
        @PrimaryKey(autoGenerate = true)
        val napId: Int = 0,
        @ColumnInfo(name = "nap_date")
        val napDate: Long,
        var start: Long = 0, var end: Long = 0
    ) {
        val duration: Long
            get() = if (end - start > 0 && start != 0L) (end - start) else 0L //in millis.
    }

    @Entity(
        tableName = "night_awakes_table",
        foreignKeys = [ForeignKey(
            entity = SleepTable::class,
            parentColumns = ["date"],
            childColumns = ["night_awake_date"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )]
    )
    class NightAwake(
        @PrimaryKey(autoGenerate = true)
        val nightAwakeId: Int = 0,
        @ColumnInfo(name = "night_awake_date")
        val nightAwakeDate: Long,
        var start: Long = 0, var end: Long = 0
    ) {
        val duration: Long
            get() = if (end - start > 0 && start != 0L) (end - start) else 0L //in millis.
    }
}