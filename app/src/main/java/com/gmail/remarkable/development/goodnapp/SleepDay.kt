package com.gmail.remarkable.development.goodnapp

data class SleepDay(
    var date: String = "unknown_date",

    var targetTWT: Long = 0,
    var wakeUp: Long = 0,
    var outOfBed: Long = 0,

    val naps: List<Nap> = MutableList(5) { Nap() },

    val targetBedtime: Long = 0,
    var realBedtime: Long = 0,
    val realTWT: Long = 0
) {
    class Nap(var start: Long = 0, var end: Long = 0) {
        val duration: Long
            get() = (end - start) //in millis.
    }
}