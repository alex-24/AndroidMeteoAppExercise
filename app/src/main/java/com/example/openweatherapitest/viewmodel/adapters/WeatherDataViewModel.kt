package com.example.openweatherapitest.viewmodel.adapters

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.openweatherapitest.BuildConfig
import com.example.openweatherapitest.R
import com.example.openweatherapitest.data.model.City
import com.example.openweatherapitest.data.model.WeatherData
import com.example.openweatherapitest.network.services.WeatherDataService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class WeatherDataViewModel: ViewModel() {

    val TAG = WeatherDataViewModel::class.java.simpleName

    val weatherData: MutableLiveData<MutableMap<String, WeatherData?>> = MutableLiveData(mutableMapOf())
    val loadingMessage: MutableLiveData<String?> = MutableLiveData(null)
    private var loadingMessageDisposable: Disposable? = null


    private val disposables: CompositeDisposable = CompositeDisposable()

    fun fetchWeatherData(context: Context, userCities: List<City>) {
        clearData()
        weatherData.value?.clear()
        startRollingLoadingMessages(context)
        Observable.intervalRange(
            0L,
            userCities.size.toLong(),
            0L,
            10,
            TimeUnit.SECONDS,
            Schedulers.io()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { stopRollingLoadingMessages() }
            .subscribe (
                { n ->
                    val i : Int = n.toInt()
                    WeatherDataService
                        .fetchWeather(city = userCities[i])
                        .subscribe (
                            { weatherData ->
                                if (BuildConfig.DEBUG) Log.d(TAG, "received data for ${this.weatherData.value!![userCities[i].name]}")
                                this.weatherData.value!![userCities[i].name] = weatherData
                                this.weatherData.postValue(this.weatherData.value)
                            }, {
                                if (BuildConfig.DEBUG) Log.e(TAG, "failed to retrieve data for ${this.weatherData.value!![userCities[i].name]}")
                                this.weatherData.value!![userCities[i].name] = null
                                this.weatherData.value = this.weatherData.value
                            }
                        ).addTo(disposables)
                },
                { error ->
                    Log.e(TAG, "Error in Rx Observable.interval flow...", error)
                    this.weatherData.value?.clear()
                }
            ).addTo(disposables)
    }

    private fun startRollingLoadingMessages(context: Context) {
        stopRollingLoadingMessages()
        this.loadingMessageDisposable = Observable.interval(
            0,
            6,
            TimeUnit.SECONDS,
            Schedulers.io()
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { n ->
                    val loadingMessages = context.resources.getStringArray(R.array.loading_messages)
                    this.loadingMessage.value = loadingMessages[(n % loadingMessages.size).toInt()]
                },
                { error ->
                    Log.e(TAG, "Error in Rx Observable.interval flow...", error)
                    this.loadingMessage.value = null
                }
            )
    }

    private fun stopRollingLoadingMessages() {
        if (this.loadingMessageDisposable?.isDisposed == false) {
            this.loadingMessageDisposable?.dispose()
        }
        this.loadingMessageDisposable = null
        this.loadingMessage.value = null
    }

    fun clearData() {
        WeatherDataService.clearRequestQueue()
        stopRollingLoadingMessages()
        this.weatherData.value = mutableMapOf()
        this.disposables.clear()
    }
}