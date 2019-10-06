package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Database.AppDatabase
import com.example.myapplication.Database.Helper.DatabaseWorker
import com.example.myapplication.Model.User
import com.example.myapplication.Presenter.ILoginPresenter
import com.example.myapplication.Presenter.LoginPresenter
import com.example.myapplication.Utils.SessionManager
import com.example.myapplication.View.ILoginView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(),ILoginView {

    private var mDb: AppDatabase? = null
    private lateinit var mDatabaseWorker: DatabaseWorker
    lateinit var session: SessionManager


    override fun onLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    internal lateinit var loginPresenter: ILoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(applicationContext)

        var user: HashMap<String, String> = session.getUserDetails()

        if(user.get(SessionManager.KEY_EMAIL) != null) {
            var email: String = user.get(SessionManager.KEY_EMAIL)!!
            edt_email.setText(email)
        }

        mDatabaseWorker = DatabaseWorker("dbWorkerThread")
        mDatabaseWorker.start()

        mDb = AppDatabase.getInstance(this)

        //init
        loginPresenter = LoginPresenter(this)

        // 세션 로그인 체크 되어있는지 확인
        if(session.isLoggedIn()) {
            var intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        //Event
        btn_login.setOnClickListener {
            val edt_email = edt_email.text.toString()
            val edt_password = edt_password.text.toString()
            val experienced = auto_login.isChecked

            val loginCode = loginPresenter.onLogin(edt_email, edt_password) // 로그인 올바른 값인지 체크
            if(loginCode == -1) {
                goLogin(edt_email, edt_password, experienced)  // 로그인 로직
            }
        }

        btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent) // 회원가입 페이지 이동
        }

    }

    private fun goLogin(edt_email:String, edt_password:String, experienced:Boolean) {

        val task = Runnable {
            val userData: User?
            userData = mDb?.userDao()?.selectUser(edt_email, edt_password)

            if(userData != null) {
                session.createLoginSession(edt_email, experienced) // 로그인 세션 생성
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent) // 메인 페이지 이동
                finish()
            } else {
                Toast.makeText(this, "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show()

            }
        }
        mDatabaseWorker.postTask(task)
    }
}
