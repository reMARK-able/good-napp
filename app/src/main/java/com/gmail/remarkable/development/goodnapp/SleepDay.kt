package com.gmail.remarkable.development.goodnapp

class SleepDay(
    var date: String = "unknown_date",

    var targetTWT: Long = 0,
    var wakeUp: Long = 0,
    var outOfBed: Long = 0,

    val naps: List<NAP>? = null
) {
    var targetBedtime: Long = 0
    var realBedtime: Long = 0
    var realTWT: Long = 0


    class NAP(var start: Long, var end: Long) {
        val duration: Long
            get() = (end - start) / 1000 / 60 //duration in minutes
    }
}