package com.example.myapplication.Presenter

import com.example.myapplication.Model.User
import com.example.myapplication.View.IRegisterView

class RegisterPresenter(internal var iRegisterView: IRegisterView):IRegisterPresenter {
    override fun onRegister(email:String, password:String, checkPassword:String):Int {
        val user = User(0, email, password)
        val registerCode = user.isDataValid()

        if(registerCode == 0) {
            iRegisterView.onRegisterError("이메일 입력하세요.")
            return 0
        }
        else if(registerCode == 1) {
            iRegisterView.onRegisterError("이메일 형식이 잘못되었습니다.")
            return 1
        }
        else if(registerCode == 2) {
            iRegisterView.onRegisterError("비밀번호 6자리 이상 입력하세요.")
            return 2
        }
        else if(password != checkPassword) {
            iRegisterView.onRegisterError("비밀번호가 일치하지 않습니다.")
            return 3
        }
        else {
            return -1
        }
    }
}