package com.example.armyprojectdbmsapk.screens.weaponDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.armyprojectdbmsapk.data.FirebaseStorageHelper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WeaponDetailsViewModel : ViewModel() {

    private val _weaponState = MutableStateFlow(WeaponDetailsState())
    val weaponState: StateFlow<WeaponDetailsState> = _weaponState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    // Add loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val firebaseHelper = FirebaseStorageHelper.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Load weapon details by document ID
     */
    fun loadWeaponByDocId(documentId: String) {
        viewModelScope.launch {
            try {
                // Set loading state to true before fetching data
                _isLoading.value = true

                // Reset current state when loading a new weapon
                _weaponState.value = WeaponDetailsState()
                _isFavorite.value = false

                // Fetch the weapon document from Firestore
                val documentSnapshot = firestore.collection("weapon")
                    .document(documentId)
                    .get()
                    .await()

                if (documentSnapshot.exists()) {
                    // Get all fields from the document
                    val name = documentSnapshot.getString("name") ?: ""
                    val weaponId = documentSnapshot.getLong("weapon_id")?.toInt() ?: 0
                    val caliber = documentSnapshot.getLong("caliber")?.toInt() ?: 0
                    val range = documentSnapshot.getLong("range")?.toString() ?: ""
                    val total = documentSnapshot.getLong("total")?.toInt() ?: 0
                    val manufacturer = documentSnapshot.getString("Manufacturer") ?: ""
                    val manufacturerDate = documentSnapshot.getString("Manufacturer date") ?: ""
                    val imageURL = documentSnapshot.getString("imageURL") ?: ""

                    // Update the state with the fetched data
                    _weaponState.value = WeaponDetailsState(
                        name = name,
                        weaponId = weaponId,
                        // These still use default values if you don't have them in Firestore
                        category = "Assault Rifle",
                        description = "Iconic, rugged, weapon known for reliability and impact.",
                        caliber = caliber,
                        range = "${range}m",
                        totalQuantity = total,
                        manufacturer = manufacturer,
                        manufacturerDate = manufacturerDate,
                        imageURL = imageURL
                    )
                } else {
                    println("Weapon not found with document ID: $documentId")
                }
            } catch (e: Exception) {
                println("Error fetching weapon details: ${e.message}")
                e.printStackTrace()
            } finally {
                // Set loading state to false after data is fetched, regardless of success or failure
                _isLoading.value = false
            }
        }
    }

    /**
     * Load weapon by numeric ID
     */
    fun loadWeaponById(weaponId: String) {
        viewModelScope.launch {
            try {
                // Set loading state to true first thing
                _isLoading.value = true

                val numericId = weaponId.toIntOrNull()

                if (numericId != null) {
                    // Query Firestore to find the document with matching weapon_id
                    val querySnapshot = firestore.collection("weapon")
                        .whereEqualTo("weapon_id", numericId)
                        .get()
                        .await()

                    if (!querySnapshot.isEmpty) {
                        // Get the first document that matches
                        val documentSnapshot = querySnapshot.documents[0]
                        // Call the document ID version to fetch full details
                        loadWeaponByDocId(documentSnapshot.id)
                    } else {
                        println("No weapon found with ID: $numericId")
                        // Make sure to set loading to false if no weapon found
                        _isLoading.value = false
                    }
                } else {
                    println("Invalid weapon ID: $weaponId")
                    // Make sure to set loading to false if invalid ID
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                println("Error searching for weapon: ${e.message}")
                e.printStackTrace()
                // Make sure to set loading to false on error
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite() {
        _isFavorite.value = !_isFavorite.value
    }
}

data class WeaponDetailsState(
    val name: String = "",
    val weaponId: Int = 0,
    val category: String? = null,
    val description: String? = null,
    val caliber: Int? = null,
    val range: String? = null,
    val totalQuantity: Int? = null,
    val manufacturer: String? = null,
    val manufacturerDate: String? = null,
    val imageURL: String? = null
)