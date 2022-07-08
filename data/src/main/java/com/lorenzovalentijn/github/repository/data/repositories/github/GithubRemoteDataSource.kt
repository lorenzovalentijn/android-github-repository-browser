package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel

interface GithubRemoteDataSource {
    suspend fun getRepositories(user: String): Result<List<RepositoryModel>>
    suspend fun getRepositoryDetails(user: String, repo: String): Result<RepositoryDetailModel>
}
