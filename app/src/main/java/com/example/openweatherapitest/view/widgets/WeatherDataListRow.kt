package com.example.openweatherapitest.view.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.openweatherapitest.BuildConfig
import com.example.openweatherapitest.data.WeatherData
import com.example.openweatherapitest.databinding.ViewWeatherDataListRowBinding

class WeatherDataListRow(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val TAG = WeatherDataListRow::class.java.simpleName


    private var bindings: ViewWeatherDataListRowBinding

    constructor(context: Context) : this(context, null, 0, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)


    init {
        this.bindings = ViewWeatherDataListRowBinding.inflate(LayoutInflater.from(context), this, true)
    }

    @SuppressLint("SetTextI18n")
    fun setData(cityName: String, data: WeatherData?) {
        if (BuildConfig.DEBUG) Log.d(TAG, "setData")
        this.bindings.cityNameTextView.text = cityName
        this.bindings.temperatureTextView.text = data?.let { "${data.main.temp}°" } ?: "error"
        this.bindings.cloudinessTextView.text = data?.let { "${data.clouds.all}" } ?: "error"
    }
}