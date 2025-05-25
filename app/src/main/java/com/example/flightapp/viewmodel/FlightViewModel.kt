package com.example.flightapp.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightapp.data.FlightDatabase
import com.example.flightapp.models.Favorite
import com.example.flightapp.repository.FlightRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

val Application.dataStore by preferencesDataStore(name = "settings")

class FlightViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = FlightRepository(FlightDatabase.getInstance(application))

    private val _selectedAirport = MutableStateFlow<String?>(null)
    val selectedAirport: StateFlow<String?> = _selectedAirport

    val searchText = MutableStateFlow("")

    val airports = searchText
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repo.getAllAirports()
            } else {
                repo.searchAirports(query)
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val destinations = selectedAirport
        .filterNotNull()
        .flatMapLatest { repo.getDestinations(it) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val favorites = repo.getFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _isShowingFavorites = MutableStateFlow(true)
    val isShowingFavorites: StateFlow<Boolean> = _isShowingFavorites

    private val SEARCH_KEY = stringPreferencesKey("search")

    init {
        loadSearchQuery()
    }

    private fun loadSearchQuery() {
        viewModelScope.launch {
            getApplication<Application>().dataStore.data.collect { prefs ->
                val savedQuery = prefs[SEARCH_KEY] ?: ""
                searchText.value = savedQuery
                _isShowingFavorites.value = savedQuery.isBlank() && _selectedAirport.value == null
            }
        }
    }

    fun saveSearchQuery(value: String) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { it[SEARCH_KEY] = value }
        }
    }

    fun setAirport(iata: String?) {
        _selectedAirport.value = iata
        _isShowingFavorites.value = iata == null && searchText.value.isBlank()
    }

    fun setShowingFavorites(value: Boolean) {
        _isShowingFavorites.value = value
    }

    fun saveFavorite(dep: String, dest: String) {
        viewModelScope.launch {
            repo.addFavorite(Favorite(departureCode = dep, destinationCode = dest))
        }
    }

    fun removeFavorite(fav: Favorite) {
        viewModelScope.launch {
            repo.removeFavorite(fav)
        }
    }
}
