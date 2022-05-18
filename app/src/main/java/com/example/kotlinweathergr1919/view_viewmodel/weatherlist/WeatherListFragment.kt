package com.example.kotlinweathergr1919.view_viewmodel.weatherlist

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.databinding.FragmentWeatherListBinding
import com.example.kotlinweathergr1919.facade.entities.City
import com.example.kotlinweathergr1919.facade.entities.Weather
import com.example.kotlinweathergr1919.utils.KEY_BUNDLE_WEATHER
import com.example.kotlinweathergr1919.utils.KEY_FAB_WEATHER_LIST
import com.example.kotlinweathergr1919.utils.KEY_SHARED_PREFERENCE
import com.example.kotlinweathergr1919.view_viewmodel.details.DetailsFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_weather_list.*
import java.io.IOException

class WeatherListFragment : Fragment(), OnItemListClickListener {

    private lateinit var viewModel: WeatherListViewModel
    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }
    private var isRussian: Boolean = true
    private val adapter = WeatherListAdapter(this)
    private lateinit var sp: SharedPreferences

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)
        val observer = Observer<WeatherListState> { data -> renderData(data) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        setFABCities()
        setFABLocation()
        sp = requireActivity().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE)
        isRussian = sp.getBoolean(KEY_FAB_WEATHER_LIST, true)
        setReloadContent(isRussian)
    }

    private fun setFABLocation() {
        binding.mainFragmentFABLocation.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // важно написать убедительную просьбу
                explain()
            }
            else -> {
                mRequestPermission()
            }
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.dialog_rationale_title))
            .setMessage(resources.getString(R.string.dialog_rationale_message))
            .setPositiveButton(resources.getString(R.string.dialog_rationale_give_access)) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun mRequestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE
        )
    }

    @SuppressLint("MissingPermission") //чтобы лишнюю проверку не писать
    private fun getLocation() {
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val providerGPS =
                    locationManager.getProvider(LocationManager.GPS_PROVIDER) // можно юзать BestProvider
                providerGPS?.let {                                                          //аналогично можно добавить еще слушатель чтобы и по таймингам ловить
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        100f,
                        locationListenerDistance
                    )
                }
            }
        }
    }

    private val locationListenerDistance = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("@@@", location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
            Toast.makeText(requireContext(), "Gps Enabled", Toast.LENGTH_SHORT).show()
            Log.d("@@@", "onProviderEnabled")
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
            Toast.makeText(requireContext(), "Gps Disabled", Toast.LENGTH_SHORT).show()
            Log.d("@@@", "onProviderDisabled")
        }
    }

    private fun getAddressByLocation(location: Location) {
        val geocoder = Geocoder(requireContext())
        val timeStump = System.currentTimeMillis()
        Thread {
            try {
                val addressText = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1000000
                )[0].adminArea //вместо adminArea можно другое из достпуных значенией поставить
                requireActivity().runOnUiThread {
                    showAddressDialog(addressText, location)
                }
            } catch (e: IOException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.check_inet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.start()
        Log.d("@@@", " ПРОШЛО  ${System.currentTimeMillis() - timeStump}")

    }

    private fun setFABCities() {
        binding.floatingActionButton.setOnClickListener {
            isRussian = !isRussian
            setReloadContent(isRussian)
            sp.edit()?.run {
                putBoolean(KEY_FAB_WEATHER_LIST, isRussian)
                apply()
            }
        }
    }

    private fun setReloadContent(isRussian: Boolean) {
        if (isRussian) {
            viewModel.getWeatherRussia()
            binding.floatingActionButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_russia
                )
            )
        } else {
            binding.floatingActionButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_earth
                )
            )
            viewModel.getWeatherWorld()
        }
    }

    private fun renderData(data: WeatherListState) {
        when (data) {
            is WeatherListState.Error -> {
                val trouble = data.error.stackTraceToString()
                loadingLayout.showSnackBar(trouble, R.string.reload) {
                    setReloadContent(isRussian)
                }
            }
            is WeatherListState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is WeatherListState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherData)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
        const val REQUEST_CODE = 998
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.container,
            DetailsFragment.newInstance(Bundle().apply {
                putParcelable(KEY_BUNDLE_WEATHER, weather)
            })
        ).addToBackStack("").commit()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    onItemClick(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.back)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }
}

fun View.showSnackBar(text: String?, actionText: Int, action: (View) -> Unit) {
    text?.let {
        Snackbar.make(this, it, Snackbar.LENGTH_SHORT).run {
            setBackgroundTint(ContextCompat.getColor(context, R.color.black))
            setTextColor(ContextCompat.getColor(context, R.color.yellow_v2))
            setText(it)
            action(actionText, R.color.red, action)
            show()
        }
    }
}


fun Snackbar.action(action: Int, color: Int, listener: (View) -> Unit) {
    setActionTextColor(ContextCompat.getColor(context, color))
    setAction(action, listener)
}


