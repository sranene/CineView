package pt.ulusofona.deisi.cm2223.g22001936_22006023.Pipocas

import android.graphics.Bitmap
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Cinema
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Filme
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.RegistoFilme

object RegistoFilmes {

    private val _registo_filmes= mutableListOf<RegistoFilme>(
        RegistoFilme(Filmes.filmes[0], cinema = Cinemas.cinemas[1],10,"21/04/2008", mutableListOf(),"Spoder-man"),
        RegistoFilme(Filmes.filmes[1], Cinemas.cinemas[2],9,"21/12/2012", mutableListOf(),"Sun go brrrrrrr"),
        RegistoFilme(Filmes.filmes[2], Cinemas.cinemas[5],10,"18/08/2016", mutableListOf(),"Kachooowwwww"),
        RegistoFilme(Filmes.filmes[3], Cinemas.cinemas[3], 10,"17/02/2015", mutableListOf(),"Maze Runner but couldn't outrun his pain lmao"),
        RegistoFilme(Filmes.filmes[4], Cinemas.cinemas[6],9,"10/02/2015", mutableListOf(),"Healed everyone but couldn't heal himself lmao")
    )

    val registo_filmes get() = _registo_filmes.toList()

    fun submit(filme: Filme, cinema: Cinema, rating: Int, data: String, photoList:MutableList<Bitmap> , observacoes: String) {
       _registo_filmes.add(RegistoFilme(filme, cinema, rating, data,photoList, observacoes))
    }

    fun getFilmeById(uuid: String): RegistoFilme {
        return _registo_filmes.find { it.uuid == uuid }!!
    }

    fun getLasts(): List<RegistoFilme> {
        var lasts : MutableList<RegistoFilme> = mutableListOf()
        lasts.add(_registo_filmes.get(_registo_filmes.size-1))
        lasts.add(_registo_filmes.get(_registo_filmes.size-2))
        return lasts
    }
}