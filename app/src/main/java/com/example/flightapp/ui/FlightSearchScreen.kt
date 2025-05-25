package com.example.flightapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightapp.viewmodel.FlightViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FlightSearchScreen(viewModel: FlightViewModel) {
    val query by viewModel.searchText.collectAsState()
    val airports by viewModel.airports.collectAsState()
    val destinations by viewModel.destinations.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                viewModel.searchText.value = it
                viewModel.saveSearchQuery(it)
            },
            label = { Text("Введите код или название аэропорта") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        if (query.isNotBlank() && destinations.isEmpty()) {
            Text("Выберите аэропорт:")
            LazyColumn {
                items(airports) { airport ->
                    TextButton(onClick = {
                        viewModel.setAirport(airport.iataCode)
                    }) {
                        Text("${airport.iataCode} - ${airport.name}")
                    }
                }
            }
        } else if (destinations.isNotEmpty()) {
            Text("Рейсы из ${viewModel.selectedAirport.value}:")
            LazyColumn {
                items(destinations) { dest ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${viewModel.selectedAirport.value} → ${dest.iataCode} - ${dest.name}")
                        Button(onClick = {
                            viewModel.saveFavorite(viewModel.selectedAirport.value!!, dest.iataCode)
                        }) {
                            Text("★")
                        }
                    }
                }
            }
        } else {
            Text("Избранные маршруты:")
            LazyColumn {
                items(favorites) {
                    Text("${it.departureCode} → ${it.destinationCode}")
                }
            }
        }
    }
}
