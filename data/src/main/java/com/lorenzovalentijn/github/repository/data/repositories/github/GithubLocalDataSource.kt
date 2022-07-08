package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel

interface GithubLocalDataSource {
    suspend fun getRepositories(): List<RepositoryDetailModel>
    suspend fun saveAll(repositories: List<RepositoryDetailModel>)
}
