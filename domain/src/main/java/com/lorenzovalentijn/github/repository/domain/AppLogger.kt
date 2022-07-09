package com.lorenzovalentijn.github.repository.domain

interface AppLogger {
    fun d(message: String)
    fun e(message: String, throwable: Throwable)
}
