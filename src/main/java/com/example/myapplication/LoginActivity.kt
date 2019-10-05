package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        mDatabaseWorker = DatabaseWorker("dbWorkerThread")
        mDatabaseWorker.start()

        mDb = AppDatabase.getInstance(this)

        //init
        loginPresenter = LoginPresenter(this)

        if(session.isLoggedIn()) {
            var i: Intent = Intent(applicationContext, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
            finish()
        }

        //Event
        btn_login1.setOnClickListener {
            val loginCode = loginPresenter.onLogin(edt_email.text.toString(), edt_password.text.toString())
            viewData()
        }

        btn_register1.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    public fun viewData() {
        val edt_email = edt_email.text.toString()
        val edt_password = edt_password.text.toString()
        val experienced = auto_login.isChecked

        val task = Runnable {
            val userData: User?
            userData = mDb?.userDao()?.selectUser(edt_email, edt_password)
            if(userData != null) {
                if(experienced) {
                    session.createLoginSession(edt_email)
                }
                var i:Intent = Intent(applicationContext, MainActivity::class.java)
                finish()
            } else {
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()

            }
        }
        mDatabaseWorker.postTask(task)
    }
    private fun showMessage(message:String) {
        AlertDialog
            .Builder(this)
            .setMessage(message)
            .create()
            .show()
    }

}
