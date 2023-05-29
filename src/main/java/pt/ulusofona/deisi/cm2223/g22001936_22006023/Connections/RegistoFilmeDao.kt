package pt.ulusofona.deisi.cm2223.g22001936_22006023.Connections

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OperationDao {

    @Insert
    fun insert(registoFilme: RegistoFilmeRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(registoFilme: List<RegistoFilmeRoom>)

    @Query("SELECT * FROM registoFilme ORDER BY data ASC")
    fun getAll(): List<RegistoFilmeRoom>

    @Query("SELECT * FROM registoFilme ORDER BY data DESC LIMIT 1")
    fun getLastEntry(): RegistoFilmeRoom?

}