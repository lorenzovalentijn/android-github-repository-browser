package com.lorenzovalentijn.github.repository.domain.repositories

import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel

interface GithubRepository {
    suspend fun getRepositories(user: String): Result<List<RepositoryDetailModel>>
    suspend fun getRepositoryDetails(user: String, repo: String): Result<RepositoryDetailModel>
}
