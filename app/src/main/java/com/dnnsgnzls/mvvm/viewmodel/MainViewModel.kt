package com.dnnsgnzls.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dnnsgnzls.mvvm.models.Hero
import com.dnnsgnzls.mvvm.models.HeroDatabase
import com.dnnsgnzls.mvvm.models.HeroRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import kotlinx.coroutines.launch

class MainViewModel(
    private val application: Application,
    private val heroRepository: HeroRepository
) : BaseViewModel(application) {

    private val disposable = CompositeDisposable()

    private val _heroList = MutableLiveData<List<Hero>>()
    val heroList: LiveData<List<Hero>>
        get() = _heroList

    private val _loading = MutableLiveData<Boolean>();
    val loading: LiveData<Boolean>
        get() = _loading

    fun fetchFromRemote() {
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

    private fun updateHeroList(heroList: List<Hero>) {
        _heroList.value = heroList
        _loading.value = false
    }

    private fun storeToDatabase(heroList: List<Hero>) {
        launch {
            val dao = HeroDatabase(getApplication()).heroDao()

            dao.deleteAll()
            dao.insertAll(heroList)
            updateHeroList(heroList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
