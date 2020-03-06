package com.abai.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.abai.weatherapp.data.dataPOJO.WeatherAPI
import com.abai.weatherapp.data.internet.HttpBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button)

        button.setOnClickListener { v -> getWeatherInformation() }

    }

    fun getWeatherInformation(){

        findViewById<EditText>(R.id.edit_text)

        HttpBuilder.getService().weather("${edit_text.text}").enqueue(object : Callback<WeatherAPI> {

            override fun onResponse(call: Call<WeatherAPI>, response: Response<WeatherAPI>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()

                    val updateAt: Long? = data?.dt?.toLong()
                    val sunriseText: Long? = data?.sys?.sunrise?.toLong()
                    val sunsetText: Long? = data?.sys?.sunset?.toLong()

                    updated_at.text = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format( Date(updateAt!!.times(1000)))
                    temp_min.text = "Min Temp: " + data.main.temp_min.toString() + "°C"
                    temp_max.text = "Max Temp: " + data.main.temp_max.toString() + "°C"
                    address.text = data.name + ", " + data.sys.country
                    status.text = data.weather.get(0).description
                    temp.text = data.main.temp.toString() + "°C"
                    sunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunriseText!!.times(1000)))
                    sunset.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunsetText!!.times(1000)))
                    wind.text = data.wind.speed.toString()
                    pressure.text = data.main.pressure.toString()
                    humidity.text = data.main.humidity.toString()

                    findViewById<ProgressBar>(R.id.loader).visibility = View.GONE

                }
                Log.i("TAG", "onResponse")

            }

            override fun onFailure(call: Call<WeatherAPI>, t: Throwable) {
                Log.i("TAG", "onFaluare")
            }
        })
    }
}
