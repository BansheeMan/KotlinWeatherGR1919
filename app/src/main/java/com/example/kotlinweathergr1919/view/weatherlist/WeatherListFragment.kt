package com.example.kotlinweathergr1919.view.weatherlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.databinding.FragmentWeatherListBinding
import com.example.kotlinweathergr1919.repository.entities.Weather
import com.example.kotlinweathergr1919.utils.KEY_BUNDLE_WEATHER
import com.example.kotlinweathergr1919.view.details.DetailsFragment
import com.example.kotlinweathergr1919.viewmodel.AppState
import com.example.kotlinweathergr1919.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_weather_list.*

class WeatherListFragment : Fragment(), OnItemListClickListener {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }
    private var isRussian = true


    private val adapter = WeatherListAdapter(this)

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
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<AppState> { data -> renderData(data) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        setFAB()
        viewModel.getWeatherRussia()

    }

    private fun setFAB() {
        binding.floatingActionButton.setOnClickListener {
            isRussian = !isRussian.also { setReloadContent(it) }
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

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                val trouble = data.error.stackTraceToString()
                loadingLayout.showSnackBar(trouble, R.string.reload) {
                    setReloadContent(isRussian)
                }
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherData)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.container,
            DetailsFragment.newInstance(Bundle().apply {
                putParcelable(KEY_BUNDLE_WEATHER, weather)
            })
        ).addToBackStack("").commit()
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


