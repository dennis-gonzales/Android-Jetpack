package com.dnnsgnzls.mvvm.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HeroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hero: Hero)

    @Insert
    suspend fun insertAll(heroList: List<Hero>)

    @Query("SELECT * FROM hero WHERE id=:heroId")
    suspend fun get(heroId: Int): Hero

    @Query("SELECT * FROM hero ORDER BY id ASC")
    suspend fun getAll(): List<Hero>

    @Update
    suspend fun update(hero: Hero)

    @Delete
    suspend fun delete(hero: Hero)

    @Query("DELETE FROM hero")
    suspend fun deleteAll()
}