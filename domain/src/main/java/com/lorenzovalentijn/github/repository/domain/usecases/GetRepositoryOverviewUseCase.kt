package com.lorenzovalentijn.github.repository.domain.usecases

import androidx.paging.PagingData
import com.lorenzovalentijn.github.repository.domain.AppLogger
import com.lorenzovalentijn.github.repository.domain.UseCase
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.repositories.GithubRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class GetRepositoryOverviewUseCase(
    private val githubRepository: GithubRepository,
    logger: AppLogger,
    coroutineDispatcher: CoroutineDispatcher,
) : UseCase<Any, Flow<PagingData<RepositoryDetailModel>>>(logger, coroutineDispatcher) {

    override suspend fun execute(parameters: Any): Flow<PagingData<RepositoryDetailModel>> {
        return githubRepository.getRepositoriesPagerFlow()
    }
}
