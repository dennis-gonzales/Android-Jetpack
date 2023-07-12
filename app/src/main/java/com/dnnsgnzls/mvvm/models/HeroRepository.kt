package com.dnnsgnzls.mvvm.models

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HeroRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val heroService = HeroService()

    suspend fun fetchHeroes(): List<Hero> {
        return withContext(dispatcher) {
            heroService.getHeroes()
        }
    }
}
