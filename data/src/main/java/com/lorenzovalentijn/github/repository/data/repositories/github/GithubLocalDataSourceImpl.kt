package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel

class GithubLocalDataSourceImpl : GithubLocalDataSource {

    override suspend fun getRepositories(): List<RepositoryModel> {
        // Map from DB to Domain
        // TODO("Not yet implemented")
        return emptyList()
    }
}
