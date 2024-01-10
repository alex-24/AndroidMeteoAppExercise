package com.example.openweatherapitest.viewmodel.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.openweatherapitest.BuildConfig
import com.example.openweatherapitest.data.WeatherData
import com.example.openweatherapitest.databinding.ViewWeatherDataListRowBinding
import com.example.openweatherapitest.view.widgets.WeatherDataListRow

class WeatherDataAdapter(private var data: MutableMap<String, WeatherData?>): RecyclerView.Adapter<WeatherDataViewHolder>() {

    private val TAG = WeatherDataAdapter::class.java.simpleName


    fun setWeatherData (data: MutableMap<String, WeatherData?>){
        this.data = data
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDataViewHolder {
        if (BuildConfig.DEBUG) Log.e(TAG, "onCreateViewHolder")
        val binding = ViewWeatherDataListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherDataViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        if (BuildConfig.DEBUG) Log.e(TAG, "getItemCount => ${this.data.keys.size}")
        return  this.data.keys.size
    }

    override fun onBindViewHolder(holder: WeatherDataViewHolder, position: Int) {
        if (BuildConfig.DEBUG) Log.e(TAG, "onBindViewHolder")
        val key = this.data.keys.elementAt(position)
        val value = this.data[key]
        holder.setData(key, value)
    }
}

class WeatherDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun setData(cityName: String, data: WeatherData?) {
        (this.itemView as? WeatherDataListRow)?.setData(cityName, data)
    }

}