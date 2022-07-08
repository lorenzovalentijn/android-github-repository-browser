package com.lorenzovalentijn.github.repository.domain.usecases

import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel
import com.lorenzovalentijn.github.repository.domain.repositories.GithubRepository

class GetRepositoryListUseCase(
    private val githubRepository: GithubRepository
) {
    suspend operator fun invoke() =
        githubRepository.getRepositories("abnamrocoesd")
}
