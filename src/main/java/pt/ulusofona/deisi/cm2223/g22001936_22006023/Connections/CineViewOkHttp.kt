package pt.ulusofona.deisi.cm2223.g22001936_22006023.Connections
import okhttp3.*
import org.json.JSONObject
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Filme
import java.io.IOException

class CineViewOkhttp(
    private val apiKey: String,
    private val client: OkHttpClient
) {

    fun searchMovie(title: String, onFinished: (Result<Filme>) -> Unit) {
        val url = "http://www.omdbapi.com/?apikey=$apiKey&t=$title"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFinished(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onFinished(Result.failure(IOException("Error: $response")))
                } else {
                    val body = response.body?.string()
                    if (body != null) {
                        val jsonResponse = JSONObject(body)
                        if (jsonResponse.has("Error")) {
                            val errorMessage = jsonResponse.getString("Error")
                            onFinished(Result.failure(IOException(errorMessage)))
                        } else {
                            val movie = parseMovieFromJson(jsonResponse)
                            onFinished(Result.success(movie))
                        }
                    }
                }
            }
        })
    }

    private fun parseMovieFromJson(json: JSONObject): Filme {
        val nome = json.getString("Title")
        val cartaz = 1//json.getString("Year")
        val genero = json.getString("Genre")
        val sinopse = json.getString("Plot")
        val atores = json.getString("Actors")
        val dataLancamento = json.getString("Released")
        val avaliacaoIMDB : Double = if(json.getString("imdbRating") != "N/A") {
            json.getString("imdbRating").toDouble()
        }else{
            0.0
        }
        val votosIMDB : Int = if(json.getString("imdbVotes") != "N/A"){
            json.getString("imdbVotes").replace(",","").toInt()
        }else{
            0
        }
        val linkIMDB : String = if(json.getString("imdbID") != "N/A"){
            "https://www.imdb.com/title/" + json.getString("imdbID")
        }else{
            "Este filme não existe no IMDB"
        }
        // Parse outros campos que você precisa do JSON do filme

        return Filme(nome,cartaz,genero,sinopse,atores,dataLancamento,avaliacaoIMDB,votosIMDB,linkIMDB)
    }
}