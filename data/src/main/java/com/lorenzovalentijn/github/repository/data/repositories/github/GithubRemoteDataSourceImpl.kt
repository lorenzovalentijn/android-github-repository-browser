package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.data.api.GithubService
import com.lorenzovalentijn.github.repository.data.mappers.RepositoryApiMapper
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel

class GithubRemoteDataSourceImpl(
    private val mapper: RepositoryApiMapper
) : GithubRemoteDataSource {

    override suspend fun getRepositories(user: String): Result<List<RepositoryDetailModel>> {
        val response = GithubService.githubService.getRepositoriesForUser(user)

        if (response.code() == 403) {
            return Result.failure(Exception("API rate limit exceeded"))
        }
        response.body()?.let {
            return Result.success(mapper.toRepositoryDetailModelList(it))
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
