package com.lorenzovalentijn.github.repository.browser

import android.app.Application
import android.content.Context
import com.lorenzovalentijn.github.repository.browser.ui.RepositoryDetailsScreen
import com.lorenzovalentijn.github.repository.data.mappers.GithubRepositoryMapper
import com.lorenzovalentijn.github.repository.data.repositories.github.GithubLocalDataSource
import com.lorenzovalentijn.github.repository.data.repositories.github.GithubLocalDataSourceImpl
import com.lorenzovalentijn.github.repository.data.repositories.github.GithubRemoteDataSource
import com.lorenzovalentijn.github.repository.data.repositories.github.GithubRemoteDataSourceImpl
import com.lorenzovalentijn.github.repository.data.repositories.github.GithubRepositoryImpl
import com.lorenzovalentijn.github.repository.domain.repositories.GithubRepository
import com.lorenzovalentijn.github.repository.domain.usecases.GetRepositoryDetailsUseCase
import com.lorenzovalentijn.github.repository.domain.usecases.GetRepositoryListUseCase
import com.lorenzovalentijn.github.repository.presentation.viewmodels.RepositoryDetailsViewModel
import com.lorenzovalentijn.github.repository.presentation.viewmodels.RepositoryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                module {
                    single<Context> { this@MainApp }
                    single { GithubRepositoryMapper() }
                    single<GithubLocalDataSource> { GithubLocalDataSourceImpl() }
                    single<GithubRemoteDataSource> { GithubRemoteDataSourceImpl(get()) }
                    single<GithubRepository> { GithubRepositoryImpl(get(), get()) }
                    single { GetRepositoryListUseCase(get()) }
                    single { GetRepositoryDetailsUseCase(get()) }
                    viewModel { RepositoryListViewModel(get()) }
                    viewModel { RepositoryDetailsViewModel(get()) }
                }
            )
        }
    }
}
