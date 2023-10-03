package com.comunidadedevspace.joaovazstudio.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: User)

    @Query("DELETE from users WHERE id =:id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
}