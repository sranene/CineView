package pt.ulusofona.deisi.cm2223.g22001936_22006023.Data

import android.content.Context
import android.util.Log
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Data.Remote.ConnectivityUtil
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.CineView
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Filme
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.RegistoFilme
import java.lang.IllegalStateException

class CineRepository private constructor(
    private val local: CineView,
    private val remote: CineView,
    private val context: Context
) : CineView() {

    override fun getFilmesRegistados(onFinished: (Result<List<RegistoFilme>>) -> Unit) {
        if (ConnectivityUtil.isOnline(context)) {
            Log.i("APP", "O aplicativo está online. Obtendo filmes do servidor...")
            remote.getFilmesRegistados { result ->
                if (result.isSuccess) {
                    val filmes = result.getOrNull()!!
                    Log.i("APP", "Obtidos ${filmes.size} filmes do servidor")
                    local.clearAllFilmesRegistados {
                        Log.i("APP", "Banco de dados limpo")
                        local.insertFilmesRegistados(filmes) {
                            onFinished(Result.success(filmes))
                        }
                    }
                } else {
                    Log.w("APP", "Erro ao obter filmes do servidor...")
                    onFinished(result)  // propagar a falha remota
                }
            }
        } else {
            Log.i("APP", "O aplicativo está offline. Obtendo filmes do banco de dados...")
            local.getFilmesRegistados(onFinished)
        }
    }

    override fun insertFilmesRegistados(filmes: List<RegistoFilme>, onFinished: () -> Unit) {
        throw Exception("Operação não permitida")
    }

    override fun clearAllFilmesRegistados(onFinished: () -> Unit) {
        throw Exception("Operação não permitida")
    }

    override fun searchMovie(title: String, context: Context, onFinished: (Result<Filme>) -> Unit) {
        throw Exception("Operação não permitida")
    }

    companion object {
        private var instance: CineRepository? = null

        fun init(local: CineView, remote: CineView, context: Context) {
            synchronized(this) {
                if (instance == null) {
                    instance = CineRepository(local, remote, context)
                }
            }
        }

        fun getInstance(): CineRepository {
            if (instance == null) {
                throw IllegalStateException("Singleton não foi inicializado")
            }
            return instance as CineRepository
        }
    }
}