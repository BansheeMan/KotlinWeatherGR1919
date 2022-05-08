package com.example.kotlinweathergr1919.view_viewmodel.historylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.databinding.FragmentHistoryWeatherListBinding
import com.example.kotlinweathergr1919.view_viewmodel.weatherlist.WeatherListState
import com.example.kotlinweathergr1919.view_viewmodel.weatherlist.showSnackBar
import kotlinx.android.synthetic.main.fragment_weather_list.*

class HistoryWeatherListFragment : Fragment() {

    private var _binding: FragmentHistoryWeatherListBinding? = null
    private val binding: FragmentHistoryWeatherListBinding
        get() {
            return _binding!!
        }
    private val adapter = HistoryWeatherListAdapter()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

     private val viewModel: HistoryWeatherListViewModel by lazy {
        ViewModelProvider(this).get(HistoryWeatherListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.getData().observe(viewLifecycleOwner) { data: WeatherListState ->
            renderData(data)
        }
        viewModel.getAll()
    }

    private fun renderData(data: WeatherListState) = when (data) {
        is WeatherListState.Error -> {
            val trouble = data.error.stackTraceToString()
            loadingLayout.showSnackBar(trouble, R.string.reload) {
            }
        }
        is WeatherListState.Loading -> {
            Toast.makeText(requireContext(),getString(R.string.history), Toast.LENGTH_SHORT).show()
        }
        is WeatherListState.Success -> {
            adapter.setData(data.weatherData)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryWeatherListFragment()
    }
}