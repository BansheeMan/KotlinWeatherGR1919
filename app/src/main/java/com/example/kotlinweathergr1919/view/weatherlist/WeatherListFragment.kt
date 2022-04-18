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
import com.example.kotlinweathergr1919.repository.Weather
import com.example.kotlinweathergr1919.utils.KEY_BUNDLE_WEATHER
import com.example.kotlinweathergr1919.view.details.DetailsFragment
import com.example.kotlinweathergr1919.viewmodel.AppState
import com.example.kotlinweathergr1919.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

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
            isRussian = !isRussian
            setReloadContent(isRussian)
        }
    }

    private fun setReloadContent(isRussian: Boolean){
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
                Snackbar
                    .make(
                        binding.floatingActionButton,
                        getString(R.string.error),
                        Snackbar.LENGTH_INDEFINITE
                    )
                    .setAction(getString(R.string.reload)) {  setReloadContent(isRussian) }
                    .show()
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
        val bundle = Bundle()
        bundle.putParcelable(KEY_BUNDLE_WEATHER, weather)
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.container,
            DetailsFragment.newInstance(bundle)
        ).addToBackStack("").commit()
    }
}