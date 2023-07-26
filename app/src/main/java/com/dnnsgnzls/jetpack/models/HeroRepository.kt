package com.dnnsgnzls.jetpack.models

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HeroRepository @Inject constructor(
    private val scheduler: Scheduler
) {
    private val heroService = HeroService()

    fun fetchHeroes(): Single<List<Hero>> {
        return heroService.getHeroes()
            .subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
    }
}
