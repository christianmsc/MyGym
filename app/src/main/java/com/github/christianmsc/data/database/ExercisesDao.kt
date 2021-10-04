package com.github.christianmsc.com.github.christianmsc.data.database

import androidx.room.*
import com.github.christianmsc.com.github.christianmsc.data.database.entities.ExercisesEntity
import com.github.christianmsc.com.github.christianmsc.data.database.entities.FavoritesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercisesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercisesEntity: ExercisesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteExercise(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM exercises_table ORDER BY id ASC")
    fun readExercises(): Flow<List<ExercisesEntity>>

    @Query("SELECT * FROM favorite_exercises_table ORDER BY id ASC")
    fun readFavoriteExercises(): Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavoriteExercise(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorite_exercises_table")
    suspend fun deleteAllFavoriteExercises()

}