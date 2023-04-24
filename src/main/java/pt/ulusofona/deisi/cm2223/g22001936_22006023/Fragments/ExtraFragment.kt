package pt.ulusofona.deisi.cm2223.g22001936_22006023.Fragments

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Adapters.FilmesAdapter
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Adapters.PosterAdapter
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Models.Filme
import pt.ulusofona.deisi.cm2223.g22001936_22006023.NavigationManager
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Pipocas.Cinemas
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Pipocas.Filmes
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Pipocas.RegistoFilmes
import pt.ulusofona.deisi.cm2223.g22001936_22006023.databinding.FragmentExtraBinding


class ExtraFragment : Fragment() {

    private lateinit var binding: FragmentExtraBinding
    private val TAG = ExtraFragment::class.java.simpleName
    private val adapter = PosterAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentExtraBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Pesquisa Por Atores"

        val adapterAtores: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.select_dialog_item, Filmes.pegarAtores())

        val actvAtores = binding.autoCompleteTextViewAtores

        actvAtores.threshold = 1 //will start working from first character

        actvAtores.setAdapter(adapterAtores)

        binding.atorLayout.setEndIconOnClickListener {
            if(binding.autoCompleteTextViewAtores.text.toString() != "" && binding.atorLayout.error == null){
                var filmes : List<Filme> = Filmes.pegarFilmesAtor(binding.autoCompleteTextViewAtores.text.toString())

                binding.rvPoster.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                binding.rvPoster.adapter = adapter

                adapter.updateItems(filmes)
            }
        }

        binding.autoCompleteTextViewAtores.doOnTextChanged { text, start, before, count ->

            if(Filmes.procurarAtor(text.toString())){
                binding.atorLayout.error = null
            }else{
                binding.atorLayout.error = "Ator inexistente, por favor escolha um da lista"
            }
        }


        return binding.root
    }

}