package com.lorenzovalentijn.github.repository.browser

import android.util.Log
import com.lorenzovalentijn.github.repository.domain.AppLogger

class LoggerImpl : AppLogger {
    private val tag = "APP"

    override fun d(message: String) {
        Log.d(tag, message)
    }

    override fun e(message: String, throwable: Throwable) {
        Log.e(tag, message, throwable)
    }
}
