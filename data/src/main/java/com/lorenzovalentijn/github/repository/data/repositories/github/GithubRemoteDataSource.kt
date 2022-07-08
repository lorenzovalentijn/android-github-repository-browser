package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel

interface GithubRemoteDataSource {
    suspend fun getRepositories(user: String): List<RepositoryModel>
    suspend fun getRepositoryDetails(user: String, repo: String): RepositoryDetailModel?
}
