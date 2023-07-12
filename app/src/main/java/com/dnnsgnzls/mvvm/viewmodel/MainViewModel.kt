package com.dnnsgnzls.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnnsgnzls.mvvm.models.Hero
import com.dnnsgnzls.mvvm.models.HeroRepository
import kotlinx.coroutines.launch

class MainViewModel(private val heroRepository: HeroRepository) : ViewModel() {

    private val _hero = MutableLiveData<List<Hero>>()
    val hero: LiveData<List<Hero>>
        get() = _hero

    fun fetchData() {
        viewModelScope.launch {
            _hero.value = heroRepository.fetchHeroes()
        }
    }
}
