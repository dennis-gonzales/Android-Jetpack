package com.dnnsgnzls.mvvm.models

import retrofit2.http.GET

interface ApiService {
    @GET("api/heroes")
    suspend fun getHeroes(): List<Hero>
}
