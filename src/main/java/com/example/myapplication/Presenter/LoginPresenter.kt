package com.example.myapplication.Presenter

import com.example.myapplication.Model.User
import com.example.myapplication.View.ILoginView

class LoginPresenter(internal var iLoginView: ILoginView):ILoginPresenter {
    override fun onLogin(email:String, password:String):Int {
        val user = User(0, email, password)
        val loginCode = user.isDataValid()

        if(loginCode == 0) {
            iLoginView.onLoginError("Email must not be null")
            return 0
        }
        else if(loginCode == 1) {
            iLoginView.onLoginError("Wrong email address")
            return 1
        }
        else if(loginCode == 2) {
            iLoginView.onLoginError("Password must be grater than 6")
            return 2
        }
        else {

            iLoginView.onLoginSuccess("Login Success")
            return -1
        }
    }
}