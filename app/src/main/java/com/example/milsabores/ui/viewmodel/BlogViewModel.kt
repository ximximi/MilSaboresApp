package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlogViewModel(
    private val blogRepository: BlogRepository // Pide el Repositorio de Blog
) : ViewModel() {

    private val _uiState = MutableStateFlow(BlogUiState())
    val uiState: StateFlow<BlogUiState> = _uiState.asStateFlow()

    init {
        obtenerEntradasDelBlog()
    }

    private fun obtenerEntradasDelBlog() {
        viewModelScope.launch {
            blogRepository.obtenerTodos() // Llama al repositorio
                .onStart {
                    _uiState.update { it.copy(estaCargando = true) }
                }
                .catch { e ->
                    _uiState.update { it.copy(estaCargando = false, error = e.message) }
                }
                .collect { listaDeEntradas ->
                    _uiState.update {
                        it.copy(
                            estaCargando = false,
                            entradas = listaDeEntradas // Guarda la lista
                        )
                    }
                }
        }
    }
}