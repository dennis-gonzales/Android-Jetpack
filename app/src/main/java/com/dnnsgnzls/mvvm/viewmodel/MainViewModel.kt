package com.dnnsgnzls.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dnnsgnzls.mvvm.models.Hero
import com.dnnsgnzls.mvvm.models.HeroRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(private val heroRepository: HeroRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val _hero = MutableLiveData<List<Hero>>()
    val hero: LiveData<List<Hero>>
        get() = _hero

    val observer = object: DisposableSingleObserver<List<Hero>>() {
        override fun onSuccess(heroList: List<Hero>) {
            _hero.value = heroList
        }

        override fun onError(e: Throwable) {
            Log.e("EEERROR", "onError: ", e)
        }
    }

    fun fetchFromRemote() {
        disposable.add(
            heroRepository.fetchHeroes()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
