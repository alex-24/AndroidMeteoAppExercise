package com.example.openweatherapitest.network.services

import com.example.openweatherapitest.data.model.City
import com.example.openweatherapitest.data.model.WeatherData
import com.example.openweatherapitest.network.CentralRequestManager
import io.reactivex.rxjava3.core.Single

class WeatherDataService {
    companion object {

        private val TAG = WeatherDataService::class.java.simpleName

        const val BASE_URL = "https://api.openweathermap.org/data/2.5/weather"
        const val PICTOGRAM_BASE_URL = "https://openweathermap.org/img/wn"
        const val API_KEY = "<insert key here>" //TODO: provide api key

        fun fetchWeather(city: City): Single<WeatherData> {
            return CentralRequestManager.fetchObject(
                baseUrl = BASE_URL,
                params = mapOf(
                    "lat" to city.lat,
                    "lon" to city.long,
                    "appid" to API_KEY,
                    "exclude" to "minutely,hourly,daily,alerts",
                ),
                responseReturnType = WeatherData::class.java,
                requestTag = TAG,
            )
        }

        fun clearRequestQueue() {
            CentralRequestManager.clearRequestQueue(TAG)
        }
    }
}