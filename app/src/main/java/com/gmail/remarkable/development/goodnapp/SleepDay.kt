package com.gmail.remarkable.development.goodnapp

data class SleepDay(
    var date: String = "unknown_date",

    var targetTWT: Long = 0,
    var wakeUp: Long = 0,
    var outOfBed: Long = 0,

    val naps: MutableList<Nap> = mutableListOf(), //MutableList(5) { Nap() }

    var realBedtime: Long = 0
) {
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
                    if (realBedtime - naps[0].end > 0 && naps[0].end != 0L) {
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
                        if (naps[i].start - naps[i - 1].end > 0 && naps[i - 1].end != 0L) {
                            val awakeTime = naps[i].start - naps[i - 1].end
                            result.add(awakeTime)
                        } else result.add(0L)
                    }
                    if (realBedtime - naps.last().end > 0 && naps.last().end != 0L) {
                        result.add(realBedtime - naps.last().end)
                    } else result.add(0L)

                    result
                }
            }
        }

    val realTWT: Long
        get() {
            var sumOfAwakeTimes = 0L
            for (awakeTime in awakeTimes) {
                if (awakeTime != 0L) sumOfAwakeTimes += awakeTime else return 0L
                //if (awakeTimes.all { it > 0L }) return awakeTimes.sum()
            }
            return sumOfAwakeTimes
        }

    class Nap(var start: Long = 0, var end: Long = 0) {
        val duration: Long
            get() = if (end - start > 0 && start != 0L) (end - start) else 0L //in millis.
    }
}