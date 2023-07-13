package com.dnnsgnzls.jetpack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Don't pass a context or activity to a ViewModel in Android's MVVM architecture.
 *
 * This avoids potential memory leaks and adheres to the ViewModel's purpose: to be lifecycle-aware and not hold references to views, like Activities or Fragments.
 * A ViewModel can outlive these views, which can cause issues if it directly references them.
 *
 * If a context is necessary, such as for database access, use the Application Context.
 *
 *
 * This global context isn't tied to a specific lifecycle. The subclass AndroidViewModel provides this, as it includes an Application reference.
 * This ensures the ViewModel is tied to the application's lifecycle, not an activity's. With this, you get an application-scoped database, no context leaks, and no need for context handling in your ViewModel."
 */
abstract class BaseViewModel(application: Application): AndroidViewModel(application), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}