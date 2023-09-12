package com.comunidadedevspace.joaovazstudio.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

//Data Acess Object - DAO
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(task: Task)

    //Deletando todos
    @Query("DELETE from task")
    suspend fun deleteAll()

    //Deletando pelo id
    @Query("DELETE from task WHERE id =:id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM task WHERE userId = :userId")
    fun getTasksByUserId(userId: Long): LiveData<List<Task>>
}