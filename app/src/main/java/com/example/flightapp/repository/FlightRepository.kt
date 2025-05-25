package com.example.flightapp.repository

import com.example.flightapp.data.FlightDatabase
import com.example.flightapp.models.Favorite

class FlightRepository(private val db: FlightDatabase) {
    fun searchAirports(query: String) = db.airportDao().searchAirports("%$query%")
    fun getDestinations(from: String) = db.airportDao().getDestinations(from)
    fun getFavorites() = db.favoriteDao().getAllFavorites()
    suspend fun addFavorite(fav: Favorite) = db.favoriteDao().insertFavorite(fav)
}