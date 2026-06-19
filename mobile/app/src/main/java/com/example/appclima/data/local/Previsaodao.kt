package com.example.appclima.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PrevisaoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(previsao: PrevisaoTempo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirTodas(previsoes: List<PrevisaoTempo>)

    // Flow permite que a UI (Entrega 2) observe automaticamente
    // qualquer mudança nos dados, sem precisar consultar manualmente.
    @Query("SELECT * FROM previsoes WHERE cidade = :cidade ORDER BY dataPrevisao ASC")
    fun buscarPorCidade(cidade: String): Flow<List<PrevisaoTempo>>

    @Query("SELECT * FROM previsoes ORDER BY dataConsulta DESC LIMIT 1")
    suspend fun buscarUltimaConsulta(): PrevisaoTempo?

    @Query("DELETE FROM previsoes WHERE cidade = :cidade")
    suspend fun limparPorCidade(cidade: String)

    @Query("DELETE FROM previsoes")
    suspend fun limparTudo()
}