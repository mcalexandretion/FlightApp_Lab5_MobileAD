package com.example.flightapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightapp.viewmodel.FlightViewModel

@Composable
fun FlightSearchScreen(viewModel: FlightViewModel) {
    val query by viewModel.searchText.collectAsState()
    val airports by viewModel.airports.collectAsState()
    val destinations by viewModel.destinations.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val selectedAirport by viewModel.selectedAirport.collectAsState()
    val isShowingFavorites by viewModel.isShowingFavorites.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { newText ->
                viewModel.searchText.value = newText
                viewModel.saveSearchQuery(newText)

                if (newText.isBlank()) {
                    viewModel.setShowingFavorites(true)
                    viewModel.setAirport(null)
                } else {
                    viewModel.setShowingFavorites(false)
                    if (selectedAirport != null) {
                        viewModel.setAirport(null)
                    }
                }
            },
            label = { Text("Введите код или название аэропорта") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        when {
            isShowingFavorites -> {
                Text("⭐ Избранные маршруты:", style = MaterialTheme.typography.titleMedium)
                if (favorites.isEmpty()) {
                    Text("Нет сохранённых маршрутов.")
                } else {
                    LazyColumn {
                        items(favorites) { fav ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${fav.departureCode} → ${fav.destinationCode}")
                                IconButton(onClick = { viewModel.removeFavorite(fav) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                                }
                            }
                        }
                    }
                }
            }

            selectedAirport == null && query.isNotBlank() -> {
                Text("Аэропорты по запросу:", style = MaterialTheme.typography.titleMedium)
                LazyColumn {
                    items(airports) { airport ->
                        TextButton(
                            onClick = {
                                viewModel.setAirport(airport.iataCode)
                                viewModel.setShowingFavorites(false)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("${airport.iataCode} - ${airport.name}")
                        }
                    }
                }
            }

            destinations.isNotEmpty() && selectedAirport != null -> {
                Text("✈ Рейсы из $selectedAirport:", style = MaterialTheme.typography.titleMedium)

                LazyColumn {
                    items(destinations) { dest ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("$selectedAirport → ${dest.iataCode} - ${dest.name}")
                            Button(onClick = {
                                viewModel.saveFavorite(selectedAirport!!, dest.iataCode)
                            }) {
                                Text("★")
                            }
                        }
                    }
                }
            }
        }
    }
}
