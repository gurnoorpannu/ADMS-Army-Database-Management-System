package com.example.armyprojectdbmsapk.screens.warScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.armyprojectdbmsapk.data.FirebaseStorageHelper
import com.example.armyprojectdbmsapk.model.War
import kotlinx.coroutines.launch

class WarScreenViewModel : ViewModel() {
    private val firebaseHelper = FirebaseStorageHelper()
    
    val pastWars = mutableStateOf<List<War>>(emptyList())
    val ongoingWars = mutableStateOf<List<War>>(emptyList())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    init {
        loadWars()
    }

    private fun loadWars() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            
            try {
                val (past, ongoing) = firebaseHelper.getAllWars()
                pastWars.value = past
                ongoingWars.value = ongoing
            } catch (e: Exception) {
                error.value = "Failed to load wars: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}