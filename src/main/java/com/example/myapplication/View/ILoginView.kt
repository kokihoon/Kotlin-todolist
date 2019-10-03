package com.example.myapplication.View

interface ILoginView {
    fun onLoginSuccess(message :String)
    fun onLoginError(message: String)
}