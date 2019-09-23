package com.gmail.remarkable.development.goodnapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DayViewModel : ViewModel() {

    val targetTwt = MutableLiveData<String>()

    init {
        Log.i("DayViewModel", "DayViewModel is created.")

        targetTwt.value = ""
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("DayViewModel", "DayViewModel is destroyed.")
    }
}