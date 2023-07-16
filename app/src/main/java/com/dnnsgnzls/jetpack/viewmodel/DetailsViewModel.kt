package com.dnnsgnzls.jetpack.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dnnsgnzls.jetpack.models.Hero
import com.dnnsgnzls.jetpack.models.HeroDatabase
import com.dnnsgnzls.jetpack.models.HeroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class DetailsViewModel(application: Application) :
    BaseViewModel(application) {
    private val _heroDetails = MutableLiveData<Hero>()

    val heroDetails: LiveData<Hero>
        get() = _heroDetails

    fun getHero(heroId: Int) {
        launch {
            try {
                ensureActive()

                val dao = HeroDatabase(getApplication()).heroDao()
                val hero = dao.get(heroId)

                withContext(Dispatchers.Main) {
                    _heroDetails.value = hero
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}