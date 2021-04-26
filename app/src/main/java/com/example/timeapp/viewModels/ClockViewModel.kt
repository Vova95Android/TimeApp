package com.example.timeapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeapp.customView.TimeView
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class ClockViewModel(): ViewModel() {

    private var startClock=false

    fun startClock(clock: TimeView){
        startClock=true
        viewModelScope.launch {
            while (startClock) {
                val cal = Calendar.getInstance()
                val hour = cal.get(Calendar.HOUR_OF_DAY)
                val min = cal.get(Calendar.MINUTE)
                val sec = cal.get(Calendar.SECOND)
                //clock.setTime(hour, min, sec)
                delay(500)
            }
            cancel()
        }
    }

    fun stopClock(){
        startClock=false
    }
}