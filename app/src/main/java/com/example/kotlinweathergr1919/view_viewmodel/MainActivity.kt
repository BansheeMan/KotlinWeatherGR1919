package com.example.kotlinweathergr1919.view_viewmodel

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.kotlinweathergr1919.MyApp
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.content_provider_hw9.WorkWithContentProviderFragment
import com.example.kotlinweathergr1919.databinding.ActivityMainBinding
import com.example.kotlinweathergr1919.utils.CHANNEL_ID_HIGH
import com.example.kotlinweathergr1919.utils.CHANNEL_ID_LOW
import com.example.kotlinweathergr1919.utils.NOTIFICATION_ID_HIGH
import com.example.kotlinweathergr1919.utils.NOTIFICATION_ID_LOW
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

        push()
    }

    private fun push() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilderLow = NotificationCompat.Builder(this, CHANNEL_ID_LOW).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle(getString(R.string.title_low))
            setContentText(getString(R.string.text_low))
            priority = NotificationManager.IMPORTANCE_LOW
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channelNameLow = "Name $CHANNEL_ID_LOW"
            val channelDescriptionLow = "Description $CHANNEL_ID_LOW"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow = NotificationChannel(CHANNEL_ID_LOW,channelNameLow,channelPriorityLow).apply {
                description = channelDescriptionLow
            }
            notificationManager.createNotificationChannel(channelLow)
        }

        notificationManager.notify(NOTIFICATION_ID_LOW, notificationBuilderLow.build())

        //##########################################################################################

        val notificationBuilderHigh=NotificationCompat.Builder(this, CHANNEL_ID_HIGH).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle(getString(R.string.title_high))
            setContentText(getString(R.string.text_high))
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameHigh = "Name $CHANNEL_ID_HIGH"
            val channelDescriptionHigh = "Description $CHANNEL_ID_HIGH"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh = NotificationChannel(CHANNEL_ID_HIGH, channelNameHigh, channelPriorityHigh).apply {
                description = channelDescriptionHigh
            }
            notificationManager.createNotificationChannel((channelHigh))
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameHigh = "Name2 $CHANNEL_ID_LOW"
            val channelDescriptionHigh = "Description2 $CHANNEL_ID_HIGH"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_DEFAULT
            val channelHigh =
                NotificationChannel(CHANNEL_ID_LOW, channelNameHigh, channelPriorityHigh).apply {
                    description = channelDescriptionHigh
                }
            notificationManager.createNotificationChannel((channelHigh))
        }


            notificationManager.notify(NOTIFICATION_ID_HIGH, notificationBuilderHigh.build())
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
