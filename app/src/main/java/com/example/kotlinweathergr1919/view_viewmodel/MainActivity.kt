package com.example.kotlinweathergr1919.view_viewmodel

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
                checkPermissionLocation()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermissionLocation() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, MapsFragment.newInstance()).addToBackStack("").commit()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                explain()
            }
            else -> {
                mRequestPermission()
            }
        }
    }

    private fun explain() {
        AlertDialog.Builder(this)
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
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), WeatherListFragment.REQUEST_CODE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkState)
    }
}
