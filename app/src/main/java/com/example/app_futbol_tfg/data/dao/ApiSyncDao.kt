package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_futbol_tfg.data.entity.ApiSyncEntity

@Dao
interface ApiSyncDao {

    // Comprueba si ya hemos consultado este equipo o liga a la API
    @Query("SELECT COUNT(*) FROM Api_Sync WHERE tipo = :tipo AND id_externo = :idExterno")
    suspend fun hasSynced(tipo: String, idExterno: Int): Int

    // Registra que hemos consultado este equipo o liga
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markAsSynced(sync: ApiSyncEntity): Long
}