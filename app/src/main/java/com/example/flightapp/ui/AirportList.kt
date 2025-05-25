package com.example.flightapp.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flightapp.models.Airport

@Composable
fun AirportList(
    airports: List<Airport>,
    onSelectAirport: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(airports) { airport ->
            TextButton(
                onClick = { onSelectAirport(airport.iataCode) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("${airport.iataCode} - ${airport.name}")
            }
        }
    }
}

