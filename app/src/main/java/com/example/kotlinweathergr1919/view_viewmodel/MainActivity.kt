package com.example.kotlinweathergr1919.view_viewmodel

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinweathergr1919.MyApp
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.content_provider_hw9.WorkWithContentProviderFragment
import com.example.kotlinweathergr1919.databinding.ActivityMainBinding
import com.example.kotlinweathergr1919.view_viewmodel.details.networkState
import com.example.kotlinweathergr1919.view_viewmodel.historylist.HistoryWeatherListFragment
import com.example.kotlinweathergr1919.view_viewmodel.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkState, filter)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(binding.root.id, WeatherListFragment.newInstance()).commit()
        Thread { MyApp.getHistoryDao().getAll() }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, HistoryWeatherListFragment.newInstance())
                    .addToBackStack("").commit()
            }
            R.id.action_work_with_content_provider -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, WorkWithContentProviderFragment.newInstance())
                    .addToBackStack("").commit()
            }
            R.id.action_menu_google_maps -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, MapsFragment.newInstance()).addToBackStack("").commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkState)
    }
}