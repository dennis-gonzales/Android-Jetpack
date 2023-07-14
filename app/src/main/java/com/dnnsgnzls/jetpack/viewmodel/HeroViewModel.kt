package com.dnnsgnzls.jetpack.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dnnsgnzls.jetpack.models.Hero
import com.dnnsgnzls.jetpack.models.HeroDatabase
import com.dnnsgnzls.jetpack.models.HeroRepository
import com.dnnsgnzls.jetpack.util.Prefs
import com.dnnsgnzls.jetpack.util.hasElapsed
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeroViewModel(
    application: Application,
    private val heroRepository: HeroRepository
) : BaseViewModel(application) {

    private val prefs = Prefs(getApplication())
    private val disposable = CompositeDisposable()

    private val _heroList = MutableLiveData<List<Hero>>()
    private val _loading = MutableLiveData<Boolean>()

    val heroList: LiveData<List<Hero>>
        get() = _heroList
    val loading: LiveData<Boolean>
        get() = _loading

    // Nanoseconds
    private val refreshTime = 1 * 15 * 1000 * 1000 * 1000L // 15 seconds

    fun refresh(hardRefresh: Boolean = false) {
        if (hardRefresh) {
            fetchFromRemote()
            return
        }

        val updateTime = prefs.getUpdateTime()

        if (updateTime == 0L || updateTime.hasElapsed(refreshTime)) {
            // Fetches fresh data
            fetchFromRemote()
            return
        }

        // Fetch from database
        fetchFromDatabase()
    }

    private fun fetchFromRemote() {
        _loading.value = true

        val heroListObserver = object : DisposableSingleObserver<List<Hero>>() {
            override fun onSuccess(heroList: List<Hero>) {
                storeToDatabase(heroList)
            }

            override fun onError(e: Throwable) {
                _loading.value = false
                e.printStackTrace()
            }
        }

        disposable.add(
            heroRepository
                .fetchHeroes()
                .subscribeWith(heroListObserver)
        )
    }

    private fun fetchFromDatabase() {
        _loading.value = true

        launch {
            try {
                ensureActive()

                val dao = HeroDatabase(getApplication()).heroDao()
                val heroList = dao.getAll()

                withContext(Dispatchers.Main) {
                    updateHeroList(heroList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateHeroList(heroList: List<Hero>) {
        _heroList.value = heroList
        _loading.value = false
    }

    private fun storeToDatabase(heroList: List<Hero>) {
        launch {
            try {
                ensureActive()

                val dao = HeroDatabase(getApplication()).heroDao()

                dao.deleteAll()
                dao.insertAll(heroList)

                withContext(Dispatchers.Main) {
                    updateHeroList(heroList)
                    prefs.saveUpdateTime(System.nanoTime())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
