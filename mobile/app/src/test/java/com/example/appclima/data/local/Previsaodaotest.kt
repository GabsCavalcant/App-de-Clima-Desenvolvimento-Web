package com.example.appclima.data.local



import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Teste instrumentado que valida a criação do banco e as
 * operações básicas de inserção/consulta. Serve como evidência
 * de que o SQLite (via Room) está configurado corretamente.
 */
@RunWith(AndroidJUnit4::class)
class PrevisaoDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: PrevisaoDao

    @Before
    fun criarBanco() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Banco em memória: criado e descartado a cada execução de teste,
        // sem afetar o banco real usado pelo app.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.previsaoDao()
    }

    @After
    @Throws(IOException::class)
    fun fecharBanco() {
        db.close()
    }

    @Test
    fun inserirEBuscarPrevisaoPorCidade() = runBlocking {
        val previsao = PrevisaoTempo(
            cidade = "São João da Boa Vista",
            dataPrevisao = "2026-06-17",
            temperaturaAtual = 22.5,
            temperaturaMin = 15.0,
            temperaturaMax = 27.0,
            descricao = "Ensolarado",
            icone = "01d",
            umidade = 60,
            velocidadeVento = 3.2,
            dataConsulta = System.currentTimeMillis()
        )

        dao.inserir(previsao)

        val resultado = dao.buscarPorCidade("São João da Boa Vista").first()

        assertEquals(1, resultado.size)
        assertEquals("Ensolarado", resultado[0].descricao)
    }

    @Test
    fun buscarUltimaConsultaRetornaRegistroMaisRecente() = runBlocking {
        val antiga = PrevisaoTempo(
            cidade = "Campinas", dataPrevisao = "2026-06-15",
            temperaturaAtual = 18.0, temperaturaMin = 12.0, temperaturaMax = 23.0,
            descricao = "Nublado", icone = "03d", umidade = 70, velocidadeVento = 2.0,
            dataConsulta = 1000L
        )
        val recente = antiga.copy(cidade = "São João da Boa Vista", dataConsulta = 2000L)

        dao.inserirTodas(listOf(antiga, recente))

        val ultima = dao.buscarUltimaConsulta()
        assertEquals("São João da Boa Vista", ultima?.cidade)
    }
}