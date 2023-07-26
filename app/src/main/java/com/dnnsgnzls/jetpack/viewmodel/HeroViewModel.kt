package com.dnnsgnzls.jetpack.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dnnsgnzls.jetpack.R
import com.dnnsgnzls.jetpack.models.Hero
import com.dnnsgnzls.jetpack.models.HeroDatabase
import com.dnnsgnzls.jetpack.models.HeroRepository
import com.dnnsgnzls.jetpack.util.Prefs
import com.dnnsgnzls.jetpack.util.hasElapsed
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HeroViewModel @Inject constructor (
    private val heroRepository: HeroRepository,
    private val application: Application
) : BaseViewModel(application) {

    private val prefs = Prefs(getApplication())
    private val disposable = CompositeDisposable()

    private val _heroList = MutableLiveData<List<Hero>>()
    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String>()

    val heroList: LiveData<List<Hero>>
        get() = _heroList
    val loading: LiveData<Boolean>
        get() = _loading
    val error: LiveData<String>
        get() = _error

    // Nanoseconds
    private val refreshTime = 1 * 15 * 1000 * 1000 * 1000L // 15 seconds

    fun refresh(hardRefresh: Boolean = false) {
        fun isHardRefreshOrCachingDisabled(): Boolean {
            _error.value = ""

            val isCachingEnabled = prefs.getIsCachingEnabled()
            return hardRefresh || isCachingEnabled == false
        }

        if (isHardRefreshOrCachingDisabled()) {
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
                _error.value = ""
            }

            override fun onError(e: Throwable) {
                _loading.value = false
                _error.value = e.message
                e.printStackTrace()

                fetchFromDatabase(asFallback = true)
            }
        }

        disposable.add(
            heroRepository
                .fetchHeroes()
                .subscribeWith(heroListObserver)
        )
    }

    private fun fetchFromDatabase(asFallback: Boolean = false) {
        _loading.value = true

        launch {
            try {
                ensureActive()

                val dao = HeroDatabase(getApplication()).heroDao()
                val heroList = dao.getAll()

                if (asFallback) {
                    val errorMsg =
                        if (heroList.isEmpty()) R.string.cache_was_empty
                        else R.string.successfully_loaded_cache

                    Toast.makeText(
                        application,
                        application.getString(errorMsg),
                        Toast.LENGTH_LONG
                    ).show()
                }

                withContext(Dispatchers.Main) {
                    updateHeroList(heroList)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    application,
                    application.getString(R.string.failed_to_fetch_db),
                    Toast.LENGTH_LONG
                ).show()
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
