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
                _heroList.value = heroList
                _loading.value = false
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


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
