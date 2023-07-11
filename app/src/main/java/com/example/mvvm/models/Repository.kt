package com.example.mvvm.models

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class Repository(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend fun fetchData(): Data {
        return withContext(ioDispatcher) {
            // Here, we're simulating a network or database operation
            delay(1000)  // Simulate network delay
            Data(1, "Example Data")
        }
    }
}
