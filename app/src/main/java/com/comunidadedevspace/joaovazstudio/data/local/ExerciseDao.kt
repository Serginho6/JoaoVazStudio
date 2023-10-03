package com.comunidadedevspace.joaovazstudio.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(exercise: Exercise)
    @Query("DELETE from exercise WHERE id =:id")
    suspend fun deleteById(id: Int)
    @Query("SELECT * FROM exercise WHERE trainId = :trainId")
    fun getExercisesByTrainId(trainId: Int): LiveData<List<Exercise>>
}