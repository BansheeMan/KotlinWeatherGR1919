package com.example.kotlinweathergr1919.viewViewModel.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.databinding.FragmentDetailsBinding
import com.example.kotlinweathergr1919.facade.entities.Weather
import com.example.kotlinweathergr1919.utils.KEY_BUNDLE_WEATHER
import com.example.kotlinweathergr1919.viewViewModel.weatherlist.showSnackBar

class DetailsFragment : Fragment(), OnServersResponse {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { onResponse(it) }
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            viewModel.getWeather(it.city)
        }
    }

    override fun onResponse(responseState: ResponseState) {
        when (responseState) {
            is ResponseState.Loading -> binding.loadingLayoutDetails.visibility = View.VISIBLE
            is ResponseState.Success -> {
                renderDataSuccess(responseState.weather)
                binding.loadingLayoutDetails.visibility = View.GONE
            }
            is ResponseState.Errors -> {
                renderDataError(responseState.responseCode)
                binding.mainView.showSnackBar(
                    "${responseState.responseCode} ${responseState.responseMessage}",
                    R.string.back
                ) {
                    activity?.onBackPressed()
                }
                binding.loadingLayoutDetails.visibility = View.GONE

            }
            is ResponseState.ErrorRanOutOfRequests -> {
                renderDataError(responseState.responseCode)
                with(binding) {
                    mainView.showSnackBar(
                        getString(responseState.responseMessage),
                        R.string.exit
                    ) {
                        requireActivity().finish()
                    }
                    temperatureLabel.text = getText(R.string.too_much_request_today)
                    temperatureLabel.visibility = View.VISIBLE
                    loadingLayoutDetails.visibility = View.GONE

                }
            }
        }
    }

    private fun renderDataSuccess(weather: Weather) {
        with(binding) {
            cityName.text = weather.city.name
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weather.city.lat.toString(),
                weather.city.lon.toString()
            )
        }
    }

    private fun renderDataError(responseCode: Int) {
        with(binding) {
            when (responseCode) {
                in 400..499 -> cityName.text = getString(R.string.client_error)
                in 500..599 -> cityName.text = getString(R.string.server_error)
                else -> cityName.text = getString(R.string.server_error)
            }
            temperatureValue.text = getString(R.string.sad_man)
            feelsLikeValue.text = getString(R.string.don_not_know)
            temperatureLabel.visibility = View.INVISIBLE
            feelsLikeLabel.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}