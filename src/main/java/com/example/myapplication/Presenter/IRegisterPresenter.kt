package com.example.myapplication.Presenter

interface IRegisterPresenter {
    fun onRegister(email:String, password:String, checkPassword:String):Int
}