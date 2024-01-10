package com.example.openweatherapitest.viewmodel.adapters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.openweatherapitest.BuildConfig
import com.example.openweatherapitest.data.City
import com.example.openweatherapitest.data.WeatherData
import com.example.openweatherapitest.data.constants.WeatherAPIConstants
import com.example.openweatherapitest.network.OpenWeatherAppRequestManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class WeatherDataViewModel: ViewModel() {

    val TAG = WeatherDataViewModel::class.java.simpleName

    val weatherData: MutableLiveData<MutableMap<String, WeatherData?>> = MutableLiveData(mutableMapOf())
    private val disposables: CompositeDisposable = CompositeDisposable()
    fun fetchWeatherData(userCities: List<City>) {
        clearDisposables()
        weatherData.value?.clear()
        Observable.intervalRange(
            0L,
            userCities.size.toLong(),
            0L,
            10,
            TimeUnit.SECONDS,
            Schedulers.io()
        ).subscribe ({ n ->
                val i : Int = n.toInt()
                OpenWeatherAppRequestManager.fetchObject(
                    baseUrl = WeatherAPIConstants.BASE_URL,
                    params = mapOf(
                        "lat" to userCities[i].lat,
                        "long" to userCities[i].long,
                        "appid" to WeatherAPIConstants.key,
                        "exclude" to "minutely,hourly,daily,alerts",
                    ),
                    responseReturnType = WeatherData::class.java
                ).subscribe ({ weatherData ->
                    if (BuildConfig.DEBUG) Log.d(TAG, "received data for ${this.weatherData.value!![userCities[i].name]}")
                    this.weatherData.value!![userCities[i].name] = weatherData
                    this.weatherData.postValue(this.weatherData.value)
                }, {
                    if (BuildConfig.DEBUG) Log.e(TAG, "failed to retrieve data for ${this.weatherData.value!![userCities[i].name]}")
                    this.weatherData.value!![userCities[i].name] = null
                    this.weatherData.value = this.weatherData.value
                }).addTo(disposables)
            }, {
                Log.e(TAG, "Error in Rx Observable.interval flow...")
        }).addTo(disposables)
    }

    fun clearDisposables() {
        this.disposables.clear()
    }
}