package com.dnnsgnzls.jetpack.models

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class HeroRepository(private val scheduler: Scheduler = Schedulers.newThread()) {

    private val heroService = HeroService()

    fun fetchHeroes(): Single<List<Hero>> {
        return heroService.getHeroes()
            .subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
    }
}
