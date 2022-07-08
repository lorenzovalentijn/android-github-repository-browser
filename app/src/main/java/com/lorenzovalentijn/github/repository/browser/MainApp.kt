package com.lorenzovalentijn.github.repository.browser

import android.app.Application
import android.content.Context
import com.lorenzovalentijn.github.repository.data.db.RepositoryDao
import com.lorenzovalentijn.github.repository.data.db.RepositoryDatabase
import com.lorenzovalentijn.github.repository.data.mappers.RepositoryApiMapper
import com.lorenzovalentijn.github.repository.data.mappers.RepositoryEntityMapper
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
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                module { single<Context> { this@MainApp } },
                data,
                domain,
                presentation,
            )
        }
    }
}

private val data = module {
    single { RepositoryApiMapper() }
    single { RepositoryEntityMapper() }
    // single<RepositoryDatabase> { RepositoryDatabase.getDatabase(get()) }
    single<GithubLocalDataSource> {
        GithubLocalDataSourceImpl(
            RepositoryDatabase.getDatabase(get()),
            get(),
            Dispatchers.IO
        )
    }
    single<GithubRemoteDataSource> { GithubRemoteDataSourceImpl(get()) }
    single<GithubRepository> { GithubRepositoryImpl(get(), get()) }
}

private val domain = module {
    single { GetRepositoryListUseCase(get()) }
    single { GetRepositoryDetailsUseCase(get()) }
}

private val presentation = module {
    viewModel { RepositoryListViewModel(get()) }
    viewModel { RepositoryDetailsViewModel(get()) }
}
