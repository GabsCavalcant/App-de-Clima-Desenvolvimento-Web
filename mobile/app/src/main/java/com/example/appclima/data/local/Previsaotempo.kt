package com.example.appclima.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa uma previsão do tempo armazenada localmente no SQLite (via Room).
 * Os campos abaixo cobrem os dados típicos retornados por APIs de clima
 * (ex.: OpenWeatherMap), mas podem ser ajustados conforme a API escolhida.
 */
@Entity(tableName = "previsoes")
data class PrevisaoTempo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val cidade: String,

    // Data a que a previsão se refere, no formato yyyy-MM-dd
    val dataPrevisao: String,

    val temperaturaAtual: Double,
    val temperaturaMin: Double,
    val temperaturaMax: Double,

    val descricao: String,   // ex.: "Ensolarado", "Chuva leve"
    val icone: String,       // código do ícone retornado pela API
    val umidade: Int,        // em %
    val velocidadeVento: Double, // em m/s ou km/h, conforme a API

    // Timestamp (epoch millis) de quando esse registro foi salvo/atualizado.
    // Útil para decidir se os dados em cache ainda são "recentes" o suficiente
    // para uso offline, ou se vale buscar dados novos na API.
    val dataConsulta: Long
)