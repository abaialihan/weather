package com.abai.weatherapp.data.internet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpBuilder {
    private var service: RetrofitService? = null

    const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

    private fun buildService(): RetrofitService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }


    public fun getService(): RetrofitService {
        if (service == null) service = buildService()
        return service!!
    }
}