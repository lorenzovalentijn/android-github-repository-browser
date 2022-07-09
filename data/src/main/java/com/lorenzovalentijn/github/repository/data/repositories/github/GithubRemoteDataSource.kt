package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel

interface GithubRemoteDataSource {
    suspend fun getRepositories(user: String, page: Int): Result<List<RepositoryDetailModel>>
    suspend fun getRepositoryDetails(user: String, repo: String): Result<RepositoryDetailModel>
}
