package com.example.appclima.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appclima.data.local.AppDatabase
import com.example.appclima.data.local.PrevisaoTempo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val previsoes: List<PrevisaoTempo> = emptyList(),
    val cidadeBuscada: String = "São João da Boa Vista",
    val carregando: Boolean = false,
    val erro: String? = null
)

class PrevisaoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).previsaoDao()

    private val _uiState = MutableStateFlow(WeatherUiState(carregando = true))
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        carregarPrevisoes("São João da Boa Vista")
    }

    fun carregarPrevisoes(cidade: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(carregando = true, erro = null, cidadeBuscada = cidade)

            dao.buscarPorCidade(cidade).collect { lista ->
                _uiState.value = _uiState.value.copy(
                    previsoes = lista,
                    carregando = false
                )
            }
        }
    }

    // Será chamado pela Entrega 3 (integração com API)
    fun atualizarCidade(novaCidade: String) {
        carregarPrevisoes(novaCidade)
    }
}