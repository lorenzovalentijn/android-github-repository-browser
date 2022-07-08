package com.lorenzovalentijn.github.repository.domain.repositories

import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel

interface GithubRepository {
    suspend fun getRepositories(user: String): Result<List<RepositoryModel>>
    suspend fun getRepositoryDetails(user: String, repo: String): Result<RepositoryDetailModel>
}
