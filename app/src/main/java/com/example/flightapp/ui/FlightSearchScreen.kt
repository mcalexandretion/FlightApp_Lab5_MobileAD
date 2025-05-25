package com.example.flightapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightapp.viewmodel.FlightViewModel
import com.example.flightapp.ui.components.FlightAppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchScreen(viewModel: FlightViewModel) {
    val query by viewModel.searchText.collectAsState()
    val airports by viewModel.airports.collectAsState()
    val destinations by viewModel.destinations.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val selectedAirport by viewModel.selectedAirport.collectAsState()
    val isShowingFavorites by viewModel.isShowingFavorites.collectAsState()
    val airportsMap = airports.associate { it.iataCode to it.name }
    val selectedAirportObj = airports.find { it.iataCode == selectedAirport }

    Scaffold(
        topBar = { FlightAppTopBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SearchInput(
                query = query,
                onQueryChange = { newText ->
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
                }
            )

            Spacer(Modifier.height(16.dp))

            when {
                isShowingFavorites -> {
                    Text("⭐ Избранные маршруты:", style = MaterialTheme.typography.titleMedium)
                    FavoritesList(
                        favorites = favorites,
                        airportsMap = airportsMap,
                        onRemoveFavorite = { fav -> viewModel.removeFavorite(fav) }
                    )
                }

                selectedAirport == null && query.isNotBlank() -> {
                    Text("Аэропорты по запросу:", style = MaterialTheme.typography.titleMedium)
                    AirportList(
                        airports = airports,
                        onSelectAirport = {
                            viewModel.setAirport(it)
                            viewModel.setShowingFavorites(false)
                        }
                    )
                }

                selectedAirportObj != null && destinations.isNotEmpty() -> {
                    DestinationList(
                        selectedAirportObj = selectedAirportObj,
                        destinations = destinations,
                        favorites = favorites,
                        onToggleFavorite = { departCode, destCode ->
                            val isFavorite = favorites.any {
                                it.departureCode == departCode && it.destinationCode == destCode
                            }
                            if (isFavorite) {
                                viewModel.removeFavoriteByCodes(departCode, destCode)
                            } else {
                                viewModel.saveFavorite(departCode, destCode)
                            }
                        }
                    )
                }
            }
        }
    }
}
