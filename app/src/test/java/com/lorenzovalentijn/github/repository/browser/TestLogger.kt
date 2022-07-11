package com.lorenzovalentijn.github.repository.browser

import com.lorenzovalentijn.github.repository.domain.AppLogger

class TestLogger : AppLogger {
    override fun d(message: String) {
        println(message)
    }

    override fun e(message: String, throwable: Throwable) {
        println(message)
    }
}
