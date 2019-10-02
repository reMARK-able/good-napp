package com.gmail.remarkable.development.goodnapp

data class SleepDay(
    var date: String = "unknown_date",

    var targetTWT: Long = 0,
    var wakeUp: Long = 0,
    var outOfBed: Long = 0,

    val naps: MutableList<Nap> = mutableListOf(), //MutableList(5) { Nap() }

    var realBedtime: Long = 0,
    val realTWT: Long = 0
) {
    val targetBedtime: Long
        get() = outOfBed + targetTWT + (naps.sumBy { (it.duration).toInt() })

    val awakeTimes: List<Long>
        get() {
            if (naps.isEmpty()) return listOf(realBedtime - outOfBed)
            if (naps.size == 1) return listOf(naps[0].start - outOfBed, realBedtime - naps[0].end)

            val result = mutableListOf<Long>()
            result.add(naps[0].start - outOfBed)
            for (i in 1 until naps.size) {
                val awakeTime = naps[i].start - naps[i - 1].end
                result.add(awakeTime)
            }
            result.add(realBedtime - naps.last().end)

            return result
        }

    class Nap(var start: Long = 0, var end: Long = 0) {
        val duration: Long
            get() = (end - start) //in millis.
    }
}