package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleBlogViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: BlogRepository
) : ViewModel() {

    private val blogId: Int = checkNotNull(savedStateHandle["blogId"])

    private val _uiState = MutableStateFlow(DetalleBlogUiState())
    val uiState: StateFlow<DetalleBlogUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.obtenerPorId(blogId)
                .catch { e -> _uiState.update { it.copy(estaCargando = false, error = e.message) } }
                .collect { entrada ->
                    _uiState.update { it.copy(estaCargando = false, entrada = entrada) }
                }
        }
    }
}