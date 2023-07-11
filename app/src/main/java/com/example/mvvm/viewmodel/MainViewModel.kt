package com.example.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.models.Data
import com.example.mvvm.models.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _data = MutableLiveData<Data>()
    val data: LiveData<Data> get() = _data

    fun fetchData() {
        viewModelScope.launch {
            _data.value = repository.fetchData()
        }
    }
}
