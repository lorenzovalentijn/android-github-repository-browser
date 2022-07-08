package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel
import com.lorenzovalentijn.github.repository.domain.repositories.GithubRepository

class GithubRepositoryImpl(
    private val local: GithubLocalDataSource,
    private val remote: GithubRemoteDataSource,
) : GithubRepository {

    override suspend fun getRepositories(user: String): Result<List<RepositoryModel>> {
        return remote.getRepositories(user)
    }

    override suspend fun getRepositoryDetails(user: String, repo: String): Result<RepositoryDetailModel> {
        return remote.getRepositoryDetails(user, repo)
    }
}
