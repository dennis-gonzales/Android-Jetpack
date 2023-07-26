package com.dnnsgnzls.jetpack.di

import android.content.Context
import com.dnnsgnzls.jetpack.models.HeroDao
import com.dnnsgnzls.jetpack.models.HeroDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideHeroDao(@ApplicationContext context: Context): HeroDao {
        return HeroDatabase(context).heroDao()
    }
}
