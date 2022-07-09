package com.lorenzovalentijn.github.repository.domain.usecases

import com.lorenzovalentijn.github.repository.domain.repositories.GithubRepository

class GetRepositoryOverviewUseCase(
    private val githubRepository: GithubRepository
) {
    suspend operator fun invoke() =
        githubRepository.getRepositories("abnamrocoesd")
}
