package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel

interface GithubLocalDataSource {
    suspend fun getRepositories(): List<RepositoryModel>
}
