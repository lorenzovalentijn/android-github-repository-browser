package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.data.api.GithubService
import com.lorenzovalentijn.github.repository.data.mappers.RepositoryApiMapper
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel

class GithubRemoteDataSourceImpl(
    private val mapper: RepositoryApiMapper
) : GithubRemoteDataSource {

    override suspend fun getRepositories(
        user: String,
        page: Int
    ): Result<List<RepositoryDetailModel>> {
        val response = GithubService.githubService.getRepositoriesForUser(
            user = user,
            page = page,
            perPage = 10
        )

        response.body()?.let {
            return Result.success(mapper.toRepositoryDetailModelList(it))
        }

        when (response.code()) {
            401 -> return Result.failure(Exception("Unauthorized."))
            403 -> return Result.failure(Exception("API rate limit exceeded."))
            404 -> return Result.failure(Exception("Not found."))
            405 -> return Result.failure(Exception("Method not allowed"))
        }

        return Result.failure(Exception("An error occurred while retrieving all repositories"))
    }

    override suspend fun getRepositoryDetails(
        user: String,
        repo: String
    ): Result<RepositoryDetailModel> {
        val response = GithubService.githubService.getRepository(user, repo)
        response.body()?.let {
            return Result.success(mapper.toRepositoryDetailModel(it))
        }

        return Result.failure(Exception("An error occurred retrieving repository details"))
    }
}
