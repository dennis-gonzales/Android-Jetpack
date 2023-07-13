package com.dnnsgnzls.jetpack.models

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface HeroApi {
    @GET("api/heroes")
    fun getHeroes(): Single<List<Hero>>
}
