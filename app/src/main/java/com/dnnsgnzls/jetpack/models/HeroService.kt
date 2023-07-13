package com.dnnsgnzls.jetpack.models

import com.dnnsgnzls.jetpack.common.constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class HeroService {

    private val retrofit = Retrofit.Builder()
        .baseUrl(constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(HeroApi::class.java)

    fun getHeroes() = retrofit.getHeroes()
}