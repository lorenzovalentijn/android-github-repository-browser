package com.lorenzovalentijn.github.repository.domain.repositories

import androidx.paging.PagingData
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    suspend fun getRepositories(user: String, page: Int, perPage: Int): Result<List<RepositoryDetailModel>>
    suspend fun getRepositoryDetails(user: String, repo: String): Result<RepositoryDetailModel>
    fun getRepositoriesPagerFlow(): Flow<PagingData<RepositoryDetailModel>>
}
