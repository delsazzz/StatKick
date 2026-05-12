package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.CompeticionEntity
import kotlinx.coroutines.flow.Flow
// @Dao marca que esta interfaz  va a ser quien hable con la BBDD
@Dao
interface CompeticionDao {
    // Flow aquí observa cambios en la BBDD, si la tabla cambia, Room emite los datos automáticamente
    @Query("SELECT * FROM Competiciones ORDER BY nombre")
    fun getAll(): Flow<List<CompeticionEntity>> 
    // id_pais = :idpais significa que Room luego el :idPais lo sustituye por el valor que pasemos como parámetro
    @Query("SELECT * FROM Competiciones WHERE id_pais = :idPais ORDER BY nombre")
    fun getByPais(idPais: Int): Flow<List<CompeticionEntity>>
    // Al haber competiciones sin país añadimos este método útil
    @Query("SELECT * FROM Competiciones WHERE id_pais IS NULL ORDER BY nombre")
    fun getSinPais(): Flow<List<CompeticionEntity>>
    // suspend indica que la función está pensada para ejecutarse dentro de una coroutine
    @Query("SELECT * FROM Competiciones WHERE id = :id")
    suspend fun getById(id: Int): CompeticionEntity?
    // .IGNORE indica que si hubiera un conflicto al insertar, Room ignora la inserción en vez de lanzar un error o reemplazar
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(competicion: CompeticionEntity): Long
    @Update
    suspend fun update(competicion: CompeticionEntity)
    @Delete
    suspend fun delete(competicion: CompeticionEntity)
}