package com.lorenzovalentijn.github.repository.browser

import android.app.Application
import android.content.Context
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                module {
                    single<Context> { this@MainApp }
                    single {
                        NewComponent(name = "Lorenzo")
                    }
                }
            )
        }
    }
}
