package com.example.armyprojectdbmsapk.screens.battalionScreen


import androidx.lifecycle.ViewModel
import com.example.armyprojectdbmsapk.data.FirebaseStorageHelper
import com.example.armyprojectdbmsapk.model.Battalion
import com.example.armyprojectdbmsapk.model.BattalionDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BattalionListViewModel : ViewModel() {
    private val repository = FirebaseStorageHelper()

    private val _battalions = MutableStateFlow<List<Battalion>>(emptyList())
    val battalions: StateFlow<List<Battalion>> = _battalions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadBattalions() {
        _isLoading.value = true
        _error.value = null

        repository.getBattalions(
            onSuccess = { battalionList ->
                _battalions.value = battalionList
                _isLoading.value = false
            },
            onError = { exception ->
                _error.value = exception.message ?: "Failed to load battalions"
                _isLoading.value = false
            }
        )
    }
}

// ViewModel for Battalion Detail Screen
class BattalionDetailViewModel : ViewModel() {
    private val repository = FirebaseStorageHelper()

    private val _battalionDetail = MutableStateFlow<BattalionDetail?>(null)
    val battalionDetail: StateFlow<BattalionDetail?> = _battalionDetail

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
}