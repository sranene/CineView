package pt.ulusofona.deisi.cm2223.g22001936_22006023.Connections

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Cinema
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Filme
import java.util.*

@Entity(tableName = "registoFilme")
data class RegistoFilmeRoom(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    @Embedded val filme: Filme,
    @Embedded val cinema: Cinema,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "data") val data: String,
    @ColumnInfo(name = "observacoes") val observacoes: String
)