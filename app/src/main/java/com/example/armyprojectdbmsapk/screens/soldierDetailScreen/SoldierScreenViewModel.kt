package com.example.armyprojectdbmsapk.screens.soldierDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.armyprojectdbmsapk.data.FirebaseStorageHelper
import com.example.armyprojectdbmsapk.model.Location
import com.example.armyprojectdbmsapk.model.Posting
import com.example.armyprojectdbmsapk.model.Soldier
import com.example.armyprojectdbmsapk.model.SoldierStatus
import com.example.armyprojectdbmsapk.model.Visited
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SoldierViewModel : ViewModel() {
    val firebaseHelper = FirebaseStorageHelper()

    // UI state
    private val _uiState = MutableStateFlow(SoldierUiState())
    val uiState: StateFlow<SoldierUiState> = _uiState.asStateFlow()

    // Search query state
    private val _searchQuery = MutableStateFlow("") // Empty by default
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // No init block needed - we'll let the user initiate searches

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchSoldierById(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                // Validate input
                val soldierId = id.toIntOrNull()
                if (soldierId == null) {
                    _errorMessage.value = "Please enter a valid soldier ID"
                    _isLoading.value = false
                    return@launch
                }

                // Fetch soldier data
                val soldier = fetchSoldierData(soldierId)
                if (soldier == null) {
                    _errorMessage.value = "Soldier with ID $soldierId not found"
                    _isLoading.value = false
                    return@launch
                }

                // Fetch related data
                val soldierStatus = fetchSoldierStatus(soldierId)
                val postings = fetchSoldierPostings(soldierId)
                
                // Fetch visits and randomly select 2 if there are more than 2
                val allVisits = fetchSoldierVisits(soldierId)
                val visits = if (allVisits.size > 2) {
                    // Randomly select 2 visits
                    allVisits.shuffled().take(2)
                } else {
                    // Use all visits if there are 2 or fewer
                    allVisits
                }

                // Fetch birth location
                val birthLocation = fetchBirthLocation(soldier)

                // Update UI state
                _uiState.value = SoldierUiState(
                    soldier = soldier,
                    soldierStatus = soldierStatus,
                    postings = postings,
                    visits = visits,
                    birthLocation = birthLocation
                )

            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchSoldierData(id: Int): Soldier? {
        val soldier = firebaseHelper.getSoldierById(id)
        println("Fetched soldier: $soldier")
        return soldier
    }
    
    private suspend fun fetchSoldierStatus(id: Int): SoldierStatus? {
        val status = firebaseHelper.getSoldierStatusById(id)
        println("Fetched soldier status: $status")
        return status
    }
    
    private suspend fun fetchSoldierPostings(id: Int): List<Posting> {
        println("Debug: Fetching postings for soldier ID: $id")
        val postings = firebaseHelper.getPostingsForSoldier(id)
        println("Debug: Fetched ${postings.size} postings: $postings")
        return postings
    }
    
    private suspend fun fetchSoldierVisits(id: Int): List<Visited> {
        val visits = firebaseHelper.getVisitsForSoldier(id)
        println("Fetched visits: $visits")
        return visits
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _uiState.value = SoldierUiState()
        _errorMessage.value = null
    }
}

// UI State class to hold all the data needed for the UI
data class SoldierUiState(
    val soldier: Soldier? = null,
    val soldierStatus: SoldierStatus? = null,
    val postings: List<Posting> = emptyList(),
    val visits: List<Visited> = emptyList(),
    val birthLocation: Location? = null
)

// Function to fetch the birth location for a soldier
private suspend fun SoldierViewModel.fetchBirthLocation(soldier: Soldier?): Location? {
    return soldier?.birthPlacePincode?.let { pincode ->
        firebaseHelper.getLocationByPincode(pincode)
    }
}
