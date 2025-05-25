package com.example.flightapp.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import com.example.flightapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightAppTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "FlightApp",
                style = AppTypography.titleSmall
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TopBar
        )
    )
}