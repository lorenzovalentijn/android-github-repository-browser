package com.lorenzovalentijn.github.repository.domain.usecases

import com.lorenzovalentijn.github.repository.domain.AppLogger
import com.lorenzovalentijn.github.repository.domain.UseCase
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.repositories.GithubRepository
import kotlinx.coroutines.CoroutineDispatcher

class GetRepositoryDetailsUseCase(
    private val githubRepository: GithubRepository,
    logger: AppLogger,
    coroutineDispatcher: CoroutineDispatcher,
) : UseCase<Pair<String, String>, RepositoryDetailModel>(logger, coroutineDispatcher) {

    override suspend fun execute(parameters: Pair<String, String>): RepositoryDetailModel {
        return githubRepository.getRepositoryDetails(parameters.first, parameters.second)
            .getOrThrow()
    }
}
