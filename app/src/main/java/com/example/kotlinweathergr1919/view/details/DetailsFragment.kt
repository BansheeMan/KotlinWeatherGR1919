package com.example.kotlinweathergr1919.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kotlinweathergr1919.BuildConfig
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.databinding.FragmentDetailsBinding
import com.example.kotlinweathergr1919.repository.OnServersResponse
import com.example.kotlinweathergr1919.repository.entities.Weather
import com.example.kotlinweathergr1919.repository.entitiesDTO.WeatherDTO
import com.example.kotlinweathergr1919.utils.*
import com.example.kotlinweathergr1919.view.weatherlist.showSnackBar
import com.example.kotlinweathergr1919.viewmodel.ResponseState
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.*
import java.io.IOException

class DetailsFragment : Fragment(), OnServersResponse {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)        //SERVICE!!!
    }

    /* private val receiver = object : BroadcastReceiver() {                                      //SERVICE!!!
         override fun onReceive(context: Context?, intent: Intent?) {
             intent?.let { intents ->
                 when (intents.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                     KEY_BUNDLE_SERVICE_BROADCAST_WEATHER -> {
                         intents.getParcelableExtra<WeatherDTO>(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)
                             ?.let {
                                 onResponse(ResponseState.Success(it))
                             }
                     }
                     KEY_BUNDLE_SERVICE_BROADCAST_ERROR -> {
                         val messageError : String? = intents.getStringExtra(KEY_BUNDLE_SERVICE_BROADCAST_ERROR)
                         val codeError = intents.getIntExtra(KEY_BUNDLE_SERVICE_BROADCAST_ERROR, 503)
                         onResponse(ResponseState.Errors(codeError, messageError.toString()))
                     }
                     KEY_BUNDLE_SERVICE_BROADCAST_ERROR_RAN_OUT_OF_REQUEST -> {
                         onResponse(ResponseState.ErrorRanOutOfRequests())
                     }
                     KEY_BUNDLE_SERVICE_BROADCAST_ERROR_OTHER -> {
                         val messageError : String? = intents.getStringExtra(KEY_BUNDLE_SERVICE_BROADCAST_ERROR_OTHER)
                         onResponse(ResponseState.Errors(responseCodeAndMessage = messageError.toString()))
                     }
                     else -> {
                         onResponse(ResponseState.Errors())
                     }
                 }
             }
         }
     }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var currentCityName: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*LocalBroadcastManager.getInstance(requireContext()).registerReceiver(            //SERVICE!!!
            receiver,
            IntentFilter(KEY_WAVE_SERVICE_BROADCAST)
        )*/
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            /* requireActivity().startService(                                               //SERVICE!!!
                 Intent(
                     requireContext(),
                     DetailsService::class.java
                 ).apply {
                     putExtra(KEY_BUNDLE_LAT, it.city.lat)
                     putExtra(KEY_BUNDLE_LON, it.city.lon)
                 }
             )*/
            getWeather(it.city.lat, it.city.lon)
        }
    }

    private fun getWeather(lat: Double, lon: Double) {
        val client = OkHttpClient()
        val builder = Request.Builder().apply {
            addHeader(YANDEX_WEATHER_API, BuildConfig.WEATHER_API_KEY)
            url("$SERVER_ANDREY$YANDEX_ENDPOINT$LAT$lat&$LON$lon")
        }
        val request = builder.build()
        val callback: Callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "onFailure", Toast.LENGTH_SHORT).show()
                    onResponse(ResponseState.Errors(responseMessage = ("$e ${e.printStackTrace()}")))
                }
            }

            override fun onResponse(call: Call, response: Response) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "onResponse", Toast.LENGTH_SHORT).show()
                }
                try {
                    if (response.isSuccessful) {
                        val weatherDTO: WeatherDTO =
                            Gson().fromJson(response.body()!!.string(), WeatherDTO::class.java)
                        requireActivity().runOnUiThread {
                            renderDataSuccess(weatherDTO)
                            Toast.makeText(requireContext(), "isSuccessful", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            onResponse(ResponseState.Errors(response.code(), response.message()))
                        }
                    }
                } catch (e: JsonSyntaxException) {    //истекло 50 запросов
                    requireActivity().runOnUiThread {
                        onResponse(ResponseState.ErrorRanOutOfRequests())
                    }
                }
            }
        }
        val call = client.newCall(request)
        call.enqueue(callback)
    }


    private fun renderDataSuccess(weatherDTO: WeatherDTO) {
        with(binding) {
            cityName.text = currentCityName
            temperatureValue.text = weatherDTO.fact.temperature.toString()
            feelsLikeValue.text = weatherDTO.fact.feelsLike.toString()
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weatherDTO.info.lat.toString(),
                weatherDTO.info.lon.toString()
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

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onResponse(responseState: ResponseState) {
        when (responseState) {
            is ResponseState.Success -> {
                renderDataSuccess(responseState.weatherDTO)
            }
            is ResponseState.Errors -> {
                renderDataError(responseState.responseCode)
                binding.mainView.showSnackBar(
                    "${responseState.responseCode} ${responseState.responseMessage}",
                    R.string.back
                ) {
                    activity?.onBackPressed()
                }
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
                }
            }
        }
    }
}