package com.example.openweatherapitest.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openweatherapitest.BuildConfig
import com.example.openweatherapitest.data.City
import com.example.openweatherapitest.databinding.FragmentWeatherDataBinding
import com.example.openweatherapitest.viewmodel.adapters.WeatherDataAdapter
import com.example.openweatherapitest.viewmodel.adapters.WeatherDataViewModel
import kotlin.math.ceil

class WeatherDataFragment : Fragment() {

    private  val TAG = WeatherDataFragment::class.java.simpleName

    private lateinit var bindings: FragmentWeatherDataBinding
    private lateinit var viewModel: WeatherDataViewModel
    private lateinit var dataAdapter: WeatherDataAdapter

    private val cities = listOf(
        City(
            name = "Rennes",
            lat = "48.1119800",
            long = "-1.6742900",
        ),
        City(
            name = "Paris",
            lat = "48.8534100",
            long = "2.3488000",
        ),
        City(
            name = "Nantes",
            lat = "47.2172500",
            long = "-1.5533600",
        ),
        City(
            name = "Bordeaux",
            lat = "44.8404400",
            long = "-0.5805000",
        ),
        City(
            name = "Lyon",
            lat = "45.7484600",
            long = "4.8467100",
        ),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.bindings = FragmentWeatherDataBinding.inflate(layoutInflater)
        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.viewModel = ViewModelProvider(requireActivity())[WeatherDataViewModel::class.java]

        this.dataAdapter = WeatherDataAdapter(viewModel.weatherData.value ?: mutableMapOf())
        this.bindings.resultsRecyclerView.layoutManager = LinearLayoutManager(context)
        this.bindings.resultsRecyclerView.adapter = dataAdapter

        Handler(Looper.getMainLooper()).postDelayed(
            {
                this.viewModel.fetchWeatherData(userCities = cities)

                this.viewModel.weatherData.observe(requireActivity()) { data ->
                    if (BuildConfig.DEBUG) Log.d(TAG, "$data")
                    val downloadProgress = (ceil(data.keys.size / 5.0 * 100)).toInt()
                    this.dataAdapter.setWeatherData(data)
                    this.bindings.progressbarButton.setProgressMode(downloadProgress)

                    if (downloadProgress == 100) {
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                this.bindings.progressbarButton.setButtonMode("Recommencer") {
                                    refreshData()
                                }
                            },
                            1000
                        )
                    }
                }
            },
            1000
        )

    }

    fun refreshData() {
        this.viewModel.fetchWeatherData(userCities = cities)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.viewModel.clearData()
    }
}