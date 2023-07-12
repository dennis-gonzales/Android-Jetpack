package com.dnnsgnzls.mvvm.models

import io.reactivex.rxjava3.core.Single

class HeroRepository() {

    private val heroService = HeroService()

    fun fetchHeroes(): Single<List<Hero>> {
        return heroService.getHeroes()
    }
}
