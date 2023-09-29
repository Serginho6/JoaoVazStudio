package com.comunidadedevspace.joaovazstudio.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TrainDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(train: Train)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(train: Train)

    @Query("DELETE from train WHERE id =:id AND userId = :userId")
    suspend fun deleteById(id: Int, userId: Long)

    @Query("SELECT * FROM train WHERE userId = :userId")
    fun getTrainsByUserId(userId: Long): LiveData<List<Train>>
}