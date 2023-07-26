package com.dnnsgnzls.jetpack.di

import com.dnnsgnzls.jetpack.models.HeroRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideScheduler(): Scheduler = Schedulers.newThread()

    @Singleton
    @Provides
    fun provideHeroRepository(scheduler: Scheduler): HeroRepository = HeroRepository(scheduler)
}
