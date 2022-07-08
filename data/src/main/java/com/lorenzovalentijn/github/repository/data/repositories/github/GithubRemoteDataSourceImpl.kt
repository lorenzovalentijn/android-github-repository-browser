package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.data.api.GithubService
import com.lorenzovalentijn.github.repository.data.mappers.GithubRepositoryMapper
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel

class GithubRemoteDataSourceImpl(
    private val mapper: GithubRepositoryMapper
) : GithubRemoteDataSource {

    override suspend fun getRepositories(user: String): Result<List<RepositoryModel>> {
        val response = GithubService.githubService.getRepositoriesForUser(user)

        if (response.code() == 403) {
            return Result.failure(Exception("API rate limit exceeded"))
        }
        response.body()?.let {
            return Result.success(mapper.toRepositoryModelList(it))
        }

        return Result.failure(Exception("An error occurred while retrieving all repositories"))
    }

    override suspend fun getRepositoryDetails(user: String, repo: String): Result<RepositoryDetailModel> {
        val response = GithubService.githubService.getRepository(user, repo)
        response.body()?.let {
            return Result.success(mapper.toRepositoryDetailModel(it))
        }

        return Result.failure(Exception("An error occurred retrieving repository details"))
    }
}
