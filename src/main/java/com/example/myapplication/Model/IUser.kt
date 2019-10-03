package com.example.myapplication.Model

interface IUser {
    val email :String
    val password:String
    fun isDataValid() :Int
}