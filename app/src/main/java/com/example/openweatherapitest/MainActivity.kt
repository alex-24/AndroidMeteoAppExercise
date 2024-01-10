package com.example.openweatherapitest

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.example.openweatherapitest.databinding.ActivityMainBinding
import com.example.openweatherapitest.view.WeatherDataFragment
import com.example.openweatherapitest.view.WelcomeFragment
import com.example.openweatherapitest.viewmodel.adapters.WeatherDataViewModel

class MainActivity : FragmentActivity() {

    private lateinit var bindings: ActivityMainBinding
    private val viewModel: WeatherDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindings = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindings.root)
        supportFragmentManager
            .beginTransaction()
            .replace(bindings.fragmentContainerView.id, WelcomeFragment())
            .commit()
    }

    fun goToMeteoFragment() {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("Meteo Page")
            .replace(bindings.fragmentContainerView.id, WeatherDataFragment())
            .commit()
    }
}

