package com.example.flightapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightapp.models.Favorite
import com.example.flightapp.ui.theme.AppTypography

@Composable
fun FavoritesList(
    favorites: List<Favorite>,
    airportsMap: Map<String, String>,
    onRemoveFavorite: (Favorite) -> Unit
) {
    if (favorites.isEmpty()) {
        Text("Нет сохранённых маршрутов.")
    } else {
        LazyColumn {
            items(favorites) { fav ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("DEPART", style = MaterialTheme.typography.labelSmall)
                        Row {
                            Text(fav.departureCode, style = AppTypography.titleLarge)
                            Spacer(Modifier.width(8.dp))
                            Text(airportsMap[fav.departureCode] ?: fav.departureCode)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("ARRIVE", style = MaterialTheme.typography.labelSmall)
                        Row {
                            Text(fav.destinationCode, style = AppTypography.titleLarge)
                            Spacer(Modifier.width(8.dp))
                            Text(airportsMap[fav.destinationCode] ?: fav.destinationCode)
                        }
                    }

                    IconButton(onClick = { onRemoveFavorite(fav) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Удалить")
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}
