package com.example.kotlinweathergr1919.view_viewmodel

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.databinding.FragmentMapsMainBinding
import com.example.kotlinweathergr1919.facade.entities.City
import com.example.kotlinweathergr1919.facade.entities.Weather
import com.example.kotlinweathergr1919.utils.KEY_BUNDLE_WEATHER
import com.example.kotlinweathergr1919.view_viewmodel.details.DetailsFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.io.IOException
import java.util.*

class MapsFragment : Fragment() {


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        map = googleMap
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        map.setOnMapLongClickListener {
            addMarkerToArray(it)
            drawLine()
        }
        getWeatherOnClick()
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true

    }

    private fun getWeatherOnClick() {
        map.setOnMapClickListener {
            try {
                val weather =
                    Weather(city = City(getAddressByLocation(it), it.latitude, it.longitude))
                requireActivity().supportFragmentManager.beginTransaction().add(
                    R.id.container,
                    DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(KEY_BUNDLE_WEATHER, weather)
                    })
                ).addToBackStack("").commit()
            } catch (e: IOException) {
                Toast.makeText(requireContext(), getString(R.string.check_inet), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getAddressByLocation(location: LatLng): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addsText: String =
            try {    //если не можеи получить название места, называем его НЕИЗВЕСТНЫМ!!!   - Сэр Фрэнсис Дрейк
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1000000
                )[0].getAddressLine(0)
            } catch (e: IndexOutOfBoundsException) {
                getString(R.string.unkown_location)
            }
        return addsText

    }


    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = arrayListOf()


    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        markers.add(marker)
    }

    private fun drawLine() {
        var previousBefore: Marker? = null
        markers.forEach { current ->
            previousBefore?.let { previous ->
                map.addPolyline(
                    PolylineOptions().add(previous.position, current.position)
                        .color(Color.GREEN)
                        .width(5f)
                )
            }
            previousBefore = current
        }
    }


    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )!!
    }

    private var _binding: FragmentMapsMainBinding? = null
    private val binding: FragmentMapsMainBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearch()
    }

    private fun initSearch() {
        binding.buttonSearch.setOnClickListener {
            val searchText = binding.searchAddress.text.toString()
            try {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val result = geocoder.getFromLocationName(searchText, 1)
                val location = LatLng(
                    result[0].latitude,
                    result[0].longitude
                )
                map.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(searchText)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                )
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location, 10f)
                )
            } catch (e: IOException) { //НИЧЕГО НЕ ВВОДИМ
                Toast.makeText(
                    requireContext(),
                    getString(R.string.enter_value),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: IndexOutOfBoundsException) { //ВВОДИМ ЕРУНДУ ВСЯКУЮ
                Toast.makeText(
                    requireContext(),
                    getString(R.string.nothing_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MapsFragment()
    }
}