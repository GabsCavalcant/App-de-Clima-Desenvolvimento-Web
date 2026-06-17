package com.example.appclima

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PrevisaoTempo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun previsaoDao(): PrevisaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Garante uma única instância do banco em todo o app (padrão Singleton),
        // evitando abrir múltiplas conexões com o SQLite ao mesmo tempo.
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "previsao_tempo_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}