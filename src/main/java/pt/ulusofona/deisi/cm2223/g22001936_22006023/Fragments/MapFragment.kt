package pt.ulusofona.deisi.cm2223.g22001936_22006023.Fragments

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Location.CineViewLocation
import pt.ulusofona.deisi.cm2223.g22001936_22006023.Location.OnLocationChangedListener
import pt.ulusofona.deisi.cm2223.g22001936_22006023.NavigationManager
import pt.ulusofona.deisi.cm2223.g22001936_22006023.R
import pt.ulusofona.deisi.cm2223.g22001936_22006023.databinding.FragmentMapBinding
import java.util.*

class MapFragment : Fragment(), OnLocationChangedListener {

    private lateinit var binding: FragmentMapBinding
    // Este objeto será utilizado para obter o nome da localidade
    private lateinit var geocoder: Geocoder
    // Esta variável irá guardar uma referência para o mapa
    private var map: GoogleMap? = null

    @SuppressLint("MissingPermission")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        binding = FragmentMapBinding.bind(view)
        geocoder = Geocoder(context, Locale.getDefault())
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync { map ->
            this.map = map
            map.isMyLocationEnabled = true
            CineViewLocation.registerListener(this)
            map.addMarker(MarkerOptions()
                .position(LatLng(38.75814, -9.15179))
                .title("ULHT")
            )
        }
        binding.fab.setOnClickListener { view ->
            NavigationManager.goToFilmesFragment(parentFragmentManager)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    // Este método será invocado sempre que a posição alterar
    override fun onLocationChanged(latitude: Double, longitude: Double) {
        placeCamera(latitude, longitude)
        placeCityName(latitude, longitude)
    }

    // Atualiza e faz zoom no mapa de acordo com a localização
    private fun placeCamera(latitude: Double, longitude: Double) {
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(latitude, longitude))
            .zoom(12f)
            .build()

        map?.animateCamera(
            CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    // Obtém a localidade do utilizador através da sua posição e coloca-a
    // numa TextView
    private fun placeCityName(latitude: Double, longitude: Double) {
        val addresses = geocoder.getFromLocation(latitude, longitude, 5)
        val location = addresses.first {
            it.locality != null && it.locality.isNotEmpty()
        }
        binding.tvCityName.text = location.locality
    }

    override fun onDestroy() {
        super.onDestroy()
        CineViewLocation.unregisterListener()
    }

}