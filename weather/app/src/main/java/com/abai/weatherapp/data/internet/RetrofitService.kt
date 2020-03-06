package com.abai.weatherapp.data.internet

import com.abai.weatherapp.data.dataPOJO.WeatherAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("weather")
    fun weather(@Query("q") city: String,
                @Query("units") units: String = "metric",
                @Query("appid") appid: String = "2a58afdf234741d7234aa914bd09943b"): Call<WeatherAPI>
}