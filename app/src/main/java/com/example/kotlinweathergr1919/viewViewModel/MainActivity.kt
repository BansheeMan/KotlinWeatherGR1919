package com.example.kotlinweathergr1919.viewViewModel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinweathergr1919.databinding.ActivityMainBinding
import com.example.kotlinweathergr1919.viewViewModel.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(binding.root.id, WeatherListFragment.newInstance()).commit()
    }
}