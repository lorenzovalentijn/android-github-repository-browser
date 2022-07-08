package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel

interface GithubRemoteDataSource {
    suspend fun getRepositories(user: String): Result<List<RepositoryDetailModel>>
    suspend fun getRepositoryDetails(user: String, repo: String): Result<RepositoryDetailModel>
}
