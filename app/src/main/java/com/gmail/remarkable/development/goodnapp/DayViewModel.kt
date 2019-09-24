package com.gmail.remarkable.development.goodnapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DayViewModel : ViewModel() {

    val nap1Start = MutableLiveData<String>()
    val nap1End = MutableLiveData<String>()
    val nap2Start = MutableLiveData<String>()
    val nap2End = MutableLiveData<String>()
    val nap3Start = MutableLiveData<String>()
    val nap3End = MutableLiveData<String>()
    val nap4Start = MutableLiveData<String>()
    val nap4End = MutableLiveData<String>()
    val nap5Start = MutableLiveData<String>()
    val nap5End = MutableLiveData<String>()


    init {
        Log.i("DayViewModel", "DayViewModel is created.")

        nap1Start.value = ""
        nap1End.value = ""
    }

    fun onTimeSet(viewId: String, time: String) {

        when (viewId) {
            NAP_1_START -> nap1Start.value = time
            NAP_1_END -> nap1End.value = time
            NAP_2_START -> nap2Start.value = time
            NAP_2_END -> nap2End.value = time
            NAP_3_START -> nap3Start.value = time
            NAP_3_END -> nap3End.value = time
            NAP_4_START -> nap4Start.value = time
            NAP_4_END -> nap4End.value = time
            NAP_5_START -> nap5Start.value = time
            NAP_5_END -> nap5End.value = time
        }
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("DayViewModel", "DayViewModel is destroyed.")
    }
}