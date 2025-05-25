package com.example.flightapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightapp.models.Airport
import com.example.flightapp.models.Favorite
import com.example.flightapp.ui.theme.AppTypography
import com.example.flightapp.ui.theme.FlightAppTheme

@Composable
fun DestinationList(
    selectedAirportObj: Airport,
    destinations: List<Airport>,
    favorites: List<Favorite>,
    onToggleFavorite: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            "Рейсы из ${selectedAirportObj.name} (${selectedAirportObj.iataCode})",
            style = AppTypography.titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(destinations) { dest ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text("DEPART", style = MaterialTheme.typography.labelSmall)
                    Row {
                        Text(selectedAirportObj.iataCode, style = AppTypography.titleLarge)
                        Spacer(Modifier.width(8.dp))
                        Text(selectedAirportObj.name)
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
                                Text(dest.iataCode, style = AppTypography.titleLarge)
                                Spacer(Modifier.width(8.dp))
                                Text(dest.name)
                            }
                        }

                        val isFavorite = favorites.any { it.departureCode == selectedAirportObj.iataCode && it.destinationCode == dest.iataCode }

                        IconButton(
                            onClick = {
                                onToggleFavorite(selectedAirportObj.iataCode, dest.iataCode)
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

