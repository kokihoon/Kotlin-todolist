package com.example.myapplication.Database.Helper

import android.os.Handler
import android.os.HandlerThread

class DatabaseWorker(threadName: String) :HandlerThread(threadName) {
    private lateinit var handler: Handler

    override fun onLooperPrepared() {
        super.onLooperPrepared()

        handler = Handler(looper)

    }

    fun postTask(task: Runnable) {
        handler.post(task)
    }
}