package com.example.flightapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightapp.viewmodel.FlightViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star

@Composable
fun FlightSearchScreen(viewModel: FlightViewModel) {
    val query by viewModel.searchText.collectAsState()
    val airports by viewModel.airports.collectAsState()
    val destinations by viewModel.destinations.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val selectedAirport by viewModel.selectedAirport.collectAsState()
    val isShowingFavorites by viewModel.isShowingFavorites.collectAsState()

    val selectedAirportObj = airports.find { it.iataCode == selectedAirport }

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
                            // Находим объекты аэропортов для отображения названий
                            val departAirport = airports.find { it.iataCode == fav.departureCode }
                            val arriveAirport = airports.find { it.iataCode == fav.destinationCode }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text("DEPART", style = MaterialTheme.typography.labelSmall)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        departAirport?.iataCode ?: fav.departureCode,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        departAirport?.name ?: "",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text("ARRIVE", style = MaterialTheme.typography.labelSmall)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        arriveAirport?.iataCode ?: fav.destinationCode,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        arriveAirport?.name ?: "",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                // Кнопка удаления избранного справа по центру
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { viewModel.removeFavorite(fav) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Удалить")
                                    }
                                }

                                Divider(modifier = Modifier.padding(vertical = 8.dp))
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

            selectedAirportObj != null && destinations.isNotEmpty() -> {
                Column {
                    Text(
                        "✈ Рейсы из ${selectedAirportObj.name} (${selectedAirportObj.iataCode})",
                        style = MaterialTheme.typography.titleMedium
                    )

                    LazyColumn {
                        items(destinations) { dest ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Text("DEPART", style = MaterialTheme.typography.labelSmall)
                                Row {
                                    Text(
                                        selectedAirportObj.iataCode,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        selectedAirportObj.name,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("ARRIVE", style = MaterialTheme.typography.labelSmall)
                                        Row {
                                            Text(
                                                dest.iataCode,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                dest.name,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }

                                    val isFavorite = favorites.any { it.departureCode == selectedAirportObj.iataCode && it.destinationCode == dest.iataCode }

                                    IconButton(
                                        onClick = {
                                            if (isFavorite) {
                                                viewModel.removeFavoriteByCodes(selectedAirportObj.iataCode, dest.iataCode)
                                            } else {
                                                viewModel.saveFavorite(selectedAirportObj.iataCode, dest.iataCode)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                                            contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else LocalContentColor.current
                                        )
                                    }
                                }

                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
