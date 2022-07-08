package com.lorenzovalentijn.github.repository.domain.usecases

import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel
import com.lorenzovalentijn.github.repository.domain.repositories.GithubRepository

class GetRepositoryDetailsUseCase(
    private val githubRepository: GithubRepository
) {
    suspend fun execute(user: String, repo: String): RepositoryDetailModel? {
        return githubRepository.getRepositoryDetails(user, repo)
    }
}
