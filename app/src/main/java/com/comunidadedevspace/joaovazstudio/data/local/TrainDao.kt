package com.comunidadedevspace.joaovazstudio.data.local

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
    @Query("DELETE from train WHERE id =:id")
    suspend fun deleteById(id: Int)
    @Query("SELECT * FROM train WHERE userUid = :userUid")
    fun getTrainsByUserId(userUid: String): LiveData<List<Train>>
    @Query("SELECT * FROM train WHERE id = :trainId")
    fun getTrainById(trainId: Int): Train?
    @Query("SELECT DISTINCT t.* FROM train t INNER JOIN exercise e ON t.id = e.trainId WHERE t.userUid = :userUid")
    fun getTrainsWithExercisesByUserId(userUid: String): LiveData<List<Train>>
}