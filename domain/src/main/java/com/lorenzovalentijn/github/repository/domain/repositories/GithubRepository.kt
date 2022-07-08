package com.lorenzovalentijn.github.repository.domain.repositories

import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel

interface GithubRepository {
    suspend fun getRepositories(user: String): List<RepositoryModel>
    suspend fun getRepositoryDetails(user: String, repo: String): RepositoryDetailModel?
}
