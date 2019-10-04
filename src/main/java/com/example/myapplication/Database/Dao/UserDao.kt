package com.example.myapplication.Database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.myapplication.Model.User

@Dao
interface UserDao {


    @Insert(onConflict= REPLACE)
    fun insert(user : User)

    @Query("SELECT * FROM users WHERE email = :email")
    fun checkEmail(email: String): User

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun selectUser(email: String, password:String):User
}