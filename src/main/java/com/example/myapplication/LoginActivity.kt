package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.Database.AppDatabase
import com.example.myapplication.Database.Helper.DatabaseWorker
import com.example.myapplication.Model.User
import com.example.myapplication.Presenter.ILoginPresenter
import com.example.myapplication.Presenter.LoginPresenter
import com.example.myapplication.View.ILoginView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(),ILoginView {

    private var mDb: AppDatabase? = null
    private lateinit var mDatabaseWorker: DatabaseWorker
    private val mUIHandler = Handler()

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

        mDatabaseWorker = DatabaseWorker("dbWorkerThread")
        mDatabaseWorker.start()

        mDb = AppDatabase.getInstance(this)

        //init
        loginPresenter = LoginPresenter(this);

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
        val task = Runnable {
            val userData: User?
            userData = mDb?.userDao()?.selectUser(edt_email.text.toString(), edt_password.text.toString())
            if(userData != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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
