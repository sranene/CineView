package pt.ulusofona.deisi.cm2223.g22001936_22006023.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.Local.Entities.RegistoFilmeDB

@Dao
interface RegistoFilmeDao {

    @Insert
    fun insert(registoFilme: RegistoFilmeDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(registoFilme: List<RegistoFilmeDB>)

    @Query("SELECT * FROM registoFilme ORDER BY data ASC")
    fun getAll(): List<RegistoFilmeDB>
    
    @Query("DELETE FROM registoFilme WHERE filmeId = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM registoFilme ORDER BY data DESC LIMIT 1")
    fun getLastEntry(): RegistoFilmeDB?

}