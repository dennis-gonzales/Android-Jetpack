package com.dnnsgnzls.mvvm.models

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL = "https://api.opendota.com"

class HeroService {

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val heroNetworkService: HeroNetworkService = retrofit.create(HeroNetworkService::class.java)

    suspend fun getHeroes() = heroNetworkService.getHeroes()
}