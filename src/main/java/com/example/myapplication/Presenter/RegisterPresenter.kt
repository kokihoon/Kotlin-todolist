package com.example.myapplication.Presenter

import com.example.myapplication.Model.User
import com.example.myapplication.View.IRegisterView

class RegisterPresenter(internal var iRegisterView: IRegisterView):IRegisterPresenter {
    override fun onRegister(email:String, password:String, checkPassword:String):Int {
        val user = User(0, email, password)
        val registerCode = user.isDataValid()

        if(registerCode == 0) {
            iRegisterView.onRegisterError("Email must not be null")
            return 0
        }
        else if(registerCode == 1) {
            iRegisterView.onRegisterError("Wrong email address")
            return 1
        }
        else if(registerCode == 2) {
            iRegisterView.onRegisterError("Password must be grater than 6")
            return 2
        }
        else {
            iRegisterView.onRegisterSuccess("Register Success")
            return -1
        }
    }
}