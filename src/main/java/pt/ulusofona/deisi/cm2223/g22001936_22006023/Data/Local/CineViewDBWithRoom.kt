package pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.Local
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.CinemaDao
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.FilmeDao
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.Local.Entities.BitmapListConverter
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.Local.Entities.CinemaDB
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.Local.Entities.FilmeDB
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.Local.Entities.RegistoFilmeDB
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.RegistoFilmeDao
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.CineView
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Cinema
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Filme
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.RegistoFilme
import java.io.ByteArrayOutputStream

class CineViewDBWithRoom(private val registoFilmeDao: RegistoFilmeDao, private val filmeDao: FilmeDao, private val cinemaDao: CinemaDao) : CineView() {

    override fun insertFilmesRegistados(filmes: List<RegistoFilme>, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            filmes.forEach {
                filmeDao.insert(FilmeDB(
                    uuid = it.filme.uuid,
                    nome = it.filme.nome,
                    cartaz = bitmapToByteArray(it.filme.cartaz),
                    genero = it.filme.genero,
                    sinopse = it.filme.sinopse,
                    atores = it.filme.atores,
                    dataLancamento = it.filme.dataLancamento,
                    avaliacaoIMDB = it.filme.avaliacaoIMDB,
                    votosIMDB = it.filme.votosIMBD,
                    linkIMDB = it.filme.linkIMDB,
                ))
                cinemaDao.insert(CinemaDB(
                    id = it.cinema.id,
                    name = it.cinema.name,
                    provider = it.cinema.provider,
                    latitude = it.cinema.latitude,
                    longitude = it.cinema.longitude,
                    address = it.cinema.address,
                    postcode = it.cinema.postcode,
                    county = it.cinema.county,
                    photos = it.cinema.photos,
                    ratings = "uns bons 20",
                    hours = "20:00 Wednesday",
                ))
            }
            filmes.map {
                var photos = it.photos.map{
                        bitmap ->  bitmapToByteArray(bitmap)
                }
                RegistoFilmeDB(
                    registoFilmeId = it.uuid,
                    filmeId = it.filme.uuid,
                    cinemaId = it.cinema.id,
                    data = it.data,
                    observacoes = it.observacoes,
                    rating = it.rating,
                    photos = photos
                )
            }.forEach {
                registoFilmeDao.insert(it)
                Log.i("APP", "Inserido ${it.filmeId} no banco de dados")
            }
            onFinished()
        }
    }

    override fun searchMovie(title: String, onFinished: (Result<Filme>) -> Unit) {
        TODO("Not yet implemented")
    }
    fun bitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        if(byteArray!= null){
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }

    override fun getFilmesRegistados(onFinished: (Result<List<RegistoFilme>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var filmesRegistados : MutableList<RegistoFilme> = mutableListOf()
            val filmes = registoFilmeDao.getAll()
            filmes.forEach {
                var photos = it.photos.map{
                        bitmap ->  byteArrayToBitmap(bitmap)
                }
                var filme = filmeDao.getFromId(it.filmeId)
                var cinema = cinemaDao.getFromId(it.cinemaId)
                filmesRegistados.add(RegistoFilme(
                    uuid = it.registoFilmeId,
                    filme = Filme(
                        filme.nome,
                        byteArrayToBitmap(filme.cartaz),
                        filme.genero,
                        filme.sinopse,
                        filme.atores,
                        filme.dataLancamento,
                        filme.avaliacaoIMDB,
                        filme.votosIMDB,
                        filme.linkIMDB
                    ),
                    cinema = Cinema(
                        cinema.id,
                        cinema.name,
                        cinema.provider,
                        cinema.latitude,
                        cinema.longitude,
                        cinema.address,
                        cinema.postcode,
                        cinema.county,
                        cinema.photos,
                        mutableListOf(),
                        mutableListOf(),
                    ),
                    data = it.data,
                    observacoes = it.observacoes,
                    rating = it.rating,
                    photos = photos
                ))
            }

            onFinished(Result.success(filmesRegistados.toList()))
        }
    }

    override fun clearFilmeRegistadoById(id: String, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            registoFilmeDao.delete(id)
            onFinished()
        }
    }

    override fun getFilmeRegistadoById(id:String,onFinished: (Result<RegistoFilme>) -> Unit){
        CoroutineScope(Dispatchers.IO).launch{
            var registoFilme = registoFilmeDao.getFromId(id)
            var filme = filmeDao.getFromId(registoFilme.filmeId)
            var cinema = cinemaDao.getFromId(registoFilme.cinemaId)
            Log.i("App","Vou pegar agora filme cartaz")
            var photos = registoFilme.photos.map{
                    bitmap ->  byteArrayToBitmap(bitmap)
            }
            var registo = RegistoFilme(
                uuid = registoFilme.registoFilmeId,
                filme = Filme(
                    filme.nome,
                    byteArrayToBitmap(filme.cartaz),
                    filme.genero,
                    filme.sinopse,
                    filme.atores,
                    filme.dataLancamento,
                    filme.avaliacaoIMDB,
                    filme.votosIMDB,
                    filme.linkIMDB
                ),
                cinema = Cinema(
                    cinema.id,
                    cinema.name,
                    cinema.provider,
                    cinema.latitude,
                    cinema.longitude,
                    cinema.address,
                    cinema.postcode,
                    cinema.county,
                    cinema.photos,
                    mutableListOf(),
                    mutableListOf(),
                ),
                data = registoFilme.data,
                observacoes = registoFilme.observacoes,
                rating = registoFilme.rating,
                photos = photos
            )
            Log.i("APPPPPPPP", "${registo.filme.cartaz}")
            Log.i("APP", "Inserido ${registoFilme.filmeId} no banco de dados")
            onFinished(Result.success(registo))
        }
    }

    override fun getUltimosRegistos(onFinished: (Result<List<RegistoFilme>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch{
            var registoFilme = registoFilmeDao.getUltimosRegistos()
            var registosFilme = registoFilme.map {
                var filme = filmeDao.getFromId(it.filmeId)
                var cinema = cinemaDao.getFromId(it.cinemaId)
                var photos = it.photos.map{
                        bitmap ->  byteArrayToBitmap(bitmap)
                }
                RegistoFilme(
                    uuid = it.registoFilmeId,
                    filme = Filme(
                        filme.nome,
                        byteArrayToBitmap(filme.cartaz),
                        filme.genero,
                        filme.sinopse,
                        filme.atores,
                        filme.dataLancamento,
                        filme.avaliacaoIMDB,
                        filme.votosIMDB,
                        filme.linkIMDB
                    ),
                    cinema = Cinema(
                        cinema.id,
                        cinema.name,
                        cinema.provider,
                        cinema.latitude,
                        cinema.longitude,
                        cinema.address,
                        cinema.postcode,
                        cinema.county,
                        cinema.photos,
                        mutableListOf(),
                        mutableListOf(),
                    ),
                    data = it.data,
                    observacoes = it.observacoes,
                    rating = it.rating,
                    photos = photos
                )
            }

            onFinished(Result.success(registosFilme))
        }
    }
    override fun insertFilmeRegistado(filme: RegistoFilme, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var photos = filme.photos.map{
                    bitmap ->  bitmapToByteArray(bitmap)
            }
            var registofilme = RegistoFilmeDB(
                                    registoFilmeId = filme.uuid,
                                    filmeId = filme.filme.uuid,
                                    cinemaId = filme.cinema.id,
                                    data = filme.data,
                                    observacoes = filme.observacoes,
                                    rating = filme.rating,
                                    photos = photos
                                )
            registoFilmeDao.insert(registofilme)
            var filmeinserir = FilmeDB(
                uuid = filme.filme.uuid,
                nome = filme.filme.nome,
                cartaz = bitmapToByteArray(filme.filme.cartaz),
                genero = filme.filme.genero,
                sinopse = filme.filme.sinopse,
                atores = filme.filme.atores,
                dataLancamento = filme.filme.dataLancamento,
                avaliacaoIMDB = filme.filme.avaliacaoIMDB,
                votosIMDB = filme.filme.votosIMBD,
                linkIMDB = filme.filme.linkIMDB,
            )
            filmeDao.insert(filmeinserir)
            cinemaDao.insert(CinemaDB(
                id = filme.cinema.id,
                name = filme.cinema.name,
                provider = filme.cinema.provider,
                latitude = filme.cinema.latitude,
                longitude = filme.cinema.longitude,
                address = filme.cinema.address,
                postcode = filme.cinema.postcode,
                county = filme.cinema.county,
                photos = filme.cinema.photos,
                ratings = "uns bons 20",
                hours = "20:00 Wednesday",
            ))
            Log.i("APP", "Inserido ${registofilme.filmeId} no banco de dados")
            Log.i("Aoo","Vamos ver ${filmeDao.getAll()}")
            Log.i("aoo", "vavo UMA CENA EM CAPS LOCK ISSO DEPOIS NAO SE REPARA ${registoFilmeDao.getAll()}")
            onFinished()
        }
    }
}