package com.example.appclima.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appclima.data.local.PrevisaoTempo
import androidx.compose.foundation.lazy.items
// ──────────────────────────────────────────────
// Tela raiz
// ──────────────────────────────────────────────
@Composable
fun WeatherScreen(viewModel: PrevisaoViewModel = viewModel(), modifier: Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    var campoBusca by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1565C0), Color(0xFF42A5F5))
                )
            )
    ) {
        // Barra de busca
        SearchBar(
            valor = campoBusca,
            aoMudar = { campoBusca = it },
            aoBuscar = {
                if (campoBusca.isNotBlank()) {
                    viewModel.atualizarCidade(campoBusca.trim())
                }
            }
        )

        when {
            uiState.carregando -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            uiState.previsoes.isEmpty() -> {
                EstadoVazio(cidade = uiState.cidadeBuscada)
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Clima atual = primeiro item da lista (mais recente)
                    item {
                        CardClimaAtual(previsao = uiState.previsoes.first())
                    }

                    // Título da seção de previsão
                    if (uiState.previsoes.size > 1) {
                        item {
                            Text(
                                text = "Próximos dias",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        // Itens de previsão (do 2º em diante)
                        val proximosDias = uiState.previsoes.drop(1)
                        items(items = proximosDias) { previsao ->
                            CardPrevisaoDia(previsao = previsao)
                        }
                    }
                }
            }
        }
    }
}

// ──────────────────────────────────────────────
// Barra de busca
// ──────────────────────────────────────────────
@Composable
fun SearchBar(valor: String, aoMudar: (String) -> Unit, aoBuscar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = valor,
            onValueChange = aoMudar,
            placeholder = { Text("Buscar cidade...", color = Color.White.copy(alpha = 0.7f)) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                cursorColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = aoBuscar,
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .size(56.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
        }
    }
}

// ──────────────────────────────────────────────
// Card: clima atual (destaque grande)
// ──────────────────────────────────────────────
@Composable
fun CardClimaAtual(previsao: PrevisaoTempo) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = previsao.cidade,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = previsao.dataPrevisao,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Temperatura em destaque
            Text(
                text = "${"%.0f".format(previsao.temperaturaAtual)}°C",
                color = Color.White,
                fontSize = 72.sp,
                fontWeight = FontWeight.Light
            )

            Text(
                text = previsao.descricao,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Linha de detalhes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DetalheClima(label = "Mín", valor = "${"%.0f".format(previsao.temperaturaMin)}°C")
                DetalheClima(label = "Máx", valor = "${"%.0f".format(previsao.temperaturaMax)}°C")
                DetalheClima(label = "Umidade", valor = "${previsao.umidade}%")
                DetalheClima(label = "Vento", valor = "${"%.1f".format(previsao.velocidadeVento)} m/s")
            }
        }
    }
}

@Composable
fun DetalheClima(label: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
    }
}

// ──────────────────────────────────────────────
// Card: linha de previsão para os próximos dias
// ──────────────────────────────────────────────
@Composable
fun CardPrevisaoDia(previsao: PrevisaoTempo) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = previsao.dataPrevisao,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = previsao.descricao,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.Center
            )
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "${"%.0f".format(previsao.temperaturaMin)}°",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                Text(
                    text = " / ${"%.0f".format(previsao.temperaturaMax)}°C",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ──────────────────────────────────────────────
// Estado vazio (banco sem dados ainda)
// ──────────────────────────────────────────────
@Composable
fun EstadoVazio(cidade: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "☁️", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Nenhuma previsão disponível\npara \"$cidade\"",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A integração com a API será feita na Entrega 3.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}