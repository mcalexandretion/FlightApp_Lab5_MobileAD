package com.example.flightapp.data

import androidx.room.*
import com.example.flightapp.models.Airport
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
//    @Query("SELECT * FROM airport WHERE LOWER(iata_code) LIKE LOWER('%' || :query || '%') OR LOWER(name) LIKE LOWER('%' || :query || '%')")
    @Query("""
      SELECT * FROM airport
      WHERE LOWER(iata_code) LIKE LOWER('%' || :query || '%')
         OR LOWER(name) LIKE LOWER('%' || :query || '%')
      ORDER BY passengers DESC
    """)
    fun searchAirports(query: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE iata_code != :from")
    fun getDestinations(from: String): Flow<List<Airport>>

//    @Query("SELECT * FROM airport")
    @Query("SELECT * FROM airport ORDER BY passengers DESC")
    fun getAllAirports(): Flow<List<Airport>>
}
