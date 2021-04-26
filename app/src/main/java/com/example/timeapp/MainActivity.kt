package com.example.timeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.timeapp.customView.TimeView
import com.example.timeapp.viewModels.ClockViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ClockViewModel
    private lateinit var clock: TimeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel=ViewModelProvider(this).get(ClockViewModel::class.java)
        clock=findViewById(R.id.timeView)
    }

    override fun onStart() {
        super.onStart()
        viewModel.startClock(clock)
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopClock()
    }
}