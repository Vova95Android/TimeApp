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


    val cal = Calendar.getInstance()
    var hour = cal.get(Calendar.HOUR_OF_DAY)
    var min = cal.get(Calendar.MINUTE)
    var sec = cal.get(Calendar.SECOND)

    fun startClock(clock: TimeView){
        startClock=true
        viewModelScope.launch {
            while (startClock) {
                clock.setTime(hour, min, sec)
                delay(1000)
                sec=clock.getSec()
                min=clock.getMin()
                hour=clock.getHour()
                sec++
                if (sec==60) {min++; sec=0}
                if (min==60) {hour++; min=0}
                if (hour==24) hour=0
            }
            cancel()
        }
    }

    fun stopClock(){
        startClock=false
    }
}