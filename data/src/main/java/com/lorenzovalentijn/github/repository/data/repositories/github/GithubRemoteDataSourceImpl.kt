package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.data.api.GithubService
import com.lorenzovalentijn.github.repository.data.mappers.GithubRepositoryMapper
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel

class GithubRemoteDataSourceImpl(
    private val mapper: GithubRepositoryMapper
) : GithubRemoteDataSource {

    override suspend fun getRepositories(user: String): List<RepositoryModel> {
        val response = GithubService.githubService.getRepositoriesForUser(user)
        response.body()?.let {
            return mapper.toRepositoryModelList(it)
        }

        return emptyList()
    }

    override suspend fun getRepositoryDetails(user: String, repo: String): RepositoryDetailModel? {
        val response = GithubService.githubService.getRepository(user, repo)
        response.body()?.let {
            return mapper.toRepositoryDetailModel(it)
        }

        return null
    }
}
