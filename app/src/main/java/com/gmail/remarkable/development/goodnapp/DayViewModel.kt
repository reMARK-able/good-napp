package com.gmail.remarkable.development.goodnapp

import android.util.Log
import androidx.lifecycle.ViewModel

class DayViewModel : ViewModel() {

    init {
        Log.i("DayViewModel", "DayViewModel is created.")
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("DayViewModel", "DayViewModel is destroyed.")
    }
}