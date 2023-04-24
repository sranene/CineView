package pt.ulusofona.deisi.cm2223.g22001936_22006023.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pt.ulusofona.deisi.cm2223.g22001936_22006023.NavigationManager
import pt.ulusofona.deisi.cm2223.g22001936_22006023.databinding.FragmentMapBinding


class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding.fab.setOnClickListener { view ->
            NavigationManager.goToFilmesFragment(parentFragmentManager)
        }
        return binding.root
    }

}