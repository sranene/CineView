package pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.Local
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.Local.Entities.RegistoFilmeDB
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.RegistoFilmeDao
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.CineView
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Filme
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.RegistoFilme

class CineViewDBWithRoom(private val registoFilmeDao: RegistoFilmeDao) : CineView() {

    override fun insertFilmesRegistados(filmes: List<RegistoFilme>, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            filmes.map {
                RegistoFilmeDB(
                    registoFilmeId = it.uuid,
                    filme = it.filme,
                    cinema = it.cinema,
                    data = it.data,
                    observacoes = it.observacoes,
                    rating = it.rating,
                    photos = it.photos
                )
            }.forEach {
                registoFilmeDao.insert(it)
                Log.i("APP", "Inserido ${it.filme.nome} no banco de dados")
            }
            onFinished()
        }
    }

    override fun getFilmesRegistados(onFinished: (Result<List<RegistoFilme>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val filmes = registoFilmeDao.getAll().map {
                RegistoFilme(
                    uuid = it.registoFilmeId,
                    filme = it.filme,
                    cinema = it.cinema,
                    data = it.data,
                    observacoes = it.observacoes,
                    rating = it.rating,
                    photos = it.photos
                )
            }
            onFinished(Result.success(filmes))
        }
    }

    override fun clearAllFilmesRegistados(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            registoFilmeDao.deleteAll()
            onFinished()
        }
    }

    override fun searchMovie(title: String, context: Context, onFinished: (Result<Filme>) -> Unit) {
        throw Exception("Operação não permitida")
    }
}