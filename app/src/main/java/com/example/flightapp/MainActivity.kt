package com.example.flightapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.flightapp.ui.FlightSearchScreen
import com.example.flightapp.viewmodel.FlightViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<FlightViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlightSearchScreen(viewModel)
        }
    }
}