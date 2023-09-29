package com.comunidadedevspace.joaovazstudio.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

//Data Acess Object - DAO
@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(exercise: Exercise)

    //Deletando todos
    @Query("DELETE from exercise")
    suspend fun deleteAll()

    //Deletando pelo id
    @Query("DELETE from exercise WHERE id =:id AND userId = :userId")
    suspend fun deleteById(id: Int, userId: Long)

    @Query("SELECT * FROM exercise WHERE userId = :userId")
    fun getExercisesByUserId(userId: Long): LiveData<List<Exercise>>
}