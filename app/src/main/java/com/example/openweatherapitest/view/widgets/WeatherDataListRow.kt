package com.example.openweatherapitest.view.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.openweatherapitest.BuildConfig
import com.example.openweatherapitest.R
import com.example.openweatherapitest.data.WeatherData
import com.example.openweatherapitest.data.constants.WeatherAPIConstants
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
        data?.let {
            this.bindings.temperatureTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
            this.bindings.cloudinessTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
            this.bindings.temperatureTextView.text = "${data.main.temp}°K" // default kelvin unit
            this.bindings.cloudinessTextView.visibility = GONE
            this.bindings.cloudinessImageView.visibility = VISIBLE
            Glide
                .with(context)
                .load("${WeatherAPIConstants.PICTOGRAM_BASE_URL}/${data.weather.firstOrNull()?.icon}.png")
                .fallback(R.drawable.ic_weather_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(this.bindings.cloudinessImageView)
            //this.bindings.cloudinessTextView.text = "${data.weather.firstOrNull()?.description}°"
        } ?: {
            this.bindings.cloudinessTextView.visibility = VISIBLE
            this.bindings.cloudinessImageView.visibility = GONE
            this.bindings.temperatureTextView.setTextColor(ContextCompat.getColor(context, R.color.red))
            this.bindings.cloudinessTextView.setTextColor(ContextCompat.getColor(context, R.color.red))
            this.bindings.temperatureTextView.text = resources.getString(R.string.error)
            this.bindings.cloudinessTextView.text = resources.getString(R.string.error)
        }
    }
}