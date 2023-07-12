package com.dnnsgnzls.mvvm.models

import retrofit2.http.GET

interface HeroNetworkService {
    @GET("api/heroes")
    suspend fun getHeroes(): List<Hero>
}
