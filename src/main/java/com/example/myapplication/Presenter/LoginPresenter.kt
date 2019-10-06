package com.example.myapplication.Presenter

import com.example.myapplication.Model.User
import com.example.myapplication.View.ILoginView

class LoginPresenter(internal var iLoginView: ILoginView):ILoginPresenter {
    override fun onLogin(email:String, password:String):Int {
        val user = User(0, email, password)
        val loginCode = user.isDataValid()

        if(loginCode == 0) {
            iLoginView.onLoginError("이메일 입력하세요.")
            return 0
        }
        else if(loginCode == 1) {
            iLoginView.onLoginError("이메일 형식이 잘못되었습니다.")
            return 1
        }
        else if(loginCode == 2) {
            iLoginView.onLoginError("비밀번호 6자리 이상 입력하세요.")
            return 2
        }
        else {
            return -1
        }
    }
}