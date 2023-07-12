package com.dnnsgnzls.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dnnsgnzls.mvvm.models.Hero
import com.dnnsgnzls.mvvm.models.HeroRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver

class MainViewModel(private val heroRepository: HeroRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val _hero = MutableLiveData<List<Hero>>()
    val hero: LiveData<List<Hero>>
        get() = _hero

    private val heroListObserver = object : DisposableSingleObserver<List<Hero>>() {
        override fun onSuccess(heroList: List<Hero>) {
            _hero.value = heroList
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }
    }

    fun fetchFromRemote() {
        disposable.add(
            heroRepository
                .fetchHeroes()
                .subscribeWith(heroListObserver)
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
