package com.example.armyprojectdbmsapk.screens.battalionDetailScreen

import androidx.lifecycle.ViewModel
import com.example.armyprojectdbmsapk.data.FirebaseStorageHelper
import com.example.armyprojectdbmsapk.model.BattalionDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BattalionDetailScreenViewModel : ViewModel() {
    private val repository = FirebaseStorageHelper()

    private val _battalionDetail = MutableStateFlow<BattalionDetail?>(null)
    val battalionDetail: StateFlow<BattalionDetail?> = _battalionDetail

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadBattalionDetail(battalionId: String) {
        _isLoading.value = true
        _error.value = null

        repository.getBattalionDetail(
            battalionId = battalionId,
            onSuccess = { detail ->
                _battalionDetail.value = detail
                _isLoading.value = false
            },
            onError = { exception ->
                _error.value = exception.message ?: "Failed to load battalion details"
                _isLoading.value = false
            }
        )
    }

    // Reload the battalion data (useful for pull-to-refresh)
    fun refreshBattalionDetail(battalionId: String) {
        loadBattalionDetail(battalionId)
    }

    // Clear any error messages
    fun clearError() {
        _error.value = null
    }
}