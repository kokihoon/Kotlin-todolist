package com.example.myapplication.Model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

interface IUser {

    val id : Int
    val email :String
    val password:String
    fun isDataValid() :Int
}