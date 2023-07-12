package com.dnnsgnzls.mvvm.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dnnsgnzls.mvvm.models.enums.RoleListTypeConverter

@Database(
    entities = [Hero::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    RoleListTypeConverter::class,
)
abstract class HeroDatabase : RoomDatabase() {

    abstract fun heroDao(): HeroDao

    companion object {
        @Volatile
        private var instance: HeroDatabase? = null

        private const val DATABASE_NAME = "hero-db";

        operator fun invoke(context: Context): HeroDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            HeroDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}