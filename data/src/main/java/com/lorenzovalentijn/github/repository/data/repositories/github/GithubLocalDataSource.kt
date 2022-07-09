package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import kotlinx.coroutines.flow.Flow

interface GithubLocalDataSource {
    suspend fun getRepositories(): Flow<List<RepositoryDetailModel>>
    suspend fun saveAll(repositories: List<RepositoryDetailModel>)
}
