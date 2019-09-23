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

    fun onTimeSet(viewId: Int, time: String) {

        when (viewId) {
            R.id.target_TWT_editText -> targetTwt.value = time
        }
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("DayViewModel", "DayViewModel is destroyed.")
    }
}