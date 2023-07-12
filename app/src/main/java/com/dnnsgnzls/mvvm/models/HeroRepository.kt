package com.dnnsgnzls.mvvm.models

import com.dnnsgnzls.mvvm.models.enums.AttackType
import com.dnnsgnzls.mvvm.models.enums.PrimaryAttr
import com.dnnsgnzls.mvvm.models.enums.Role
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class HeroRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend fun fetchHeroes(): List<Hero> {
        return withContext(dispatcher) {
            // Here, we're simulating a network or database operation
            delay(1000)  // Simulate network delay
            val x = arrayListOf<Int>()
            listOf(
                Hero(
                    1,
                    "antimage",
                    "Anti-Mage",
                    PrimaryAttr.Agi,
                    AttackType.Melee,
                    listOf(Role.Carry, Role.Escape, Role.Nuker),
                    2
                )
            )
        }
    }
}
