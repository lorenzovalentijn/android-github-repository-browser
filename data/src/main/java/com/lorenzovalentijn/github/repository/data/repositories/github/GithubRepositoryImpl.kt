package com.lorenzovalentijn.github.repository.data.repositories.github

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lorenzovalentijn.github.repository.data.api.PAGE_SIZE
import com.lorenzovalentijn.github.repository.data.db.RepositoryDatabase
import com.lorenzovalentijn.github.repository.data.mappers.RepositoryEntityMapper
import com.lorenzovalentijn.github.repository.domain.AppLogger
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.repositories.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class GithubRepositoryImpl(
    private val logger: AppLogger,
    private val local: GithubLocalDataSource,
    private val remote: GithubRemoteDataSource,
    private val repositoryDatabase: RepositoryDatabase,
    private val repositoriesPagingSource: RepositoriesPagingSource,
    private val repositoriesRemoteMediator: RepositoriesRemoteMediator,
    private val repositoryEntityMapper: RepositoryEntityMapper,
    ) : GithubRepository {

    override suspend fun getRepositories(user: String, page: Int, perPage: Int): Result<List<RepositoryDetailModel>> {
        val repositories = remote.getRepositories(user, page, perPage)
        repositories.onSuccess {
            local.saveAll(it)
        }

        return repositories
    }

    override suspend fun getRepositoryDetails(
        user: String,
        repo: String
    ): Result<RepositoryDetailModel> {

        var response: RepositoryDetailModel? = try {
            remote.getRepositoryDetails(user, repo).getOrNull()
        } catch (e: Exception) {
            null
        }

        if (response == null) {
            response = withContext(Dispatchers.IO) {
                repositoryEntityMapper.toRepositoryDetailModel(
                    repositoryDatabase.repositoryDao().findByFullName("$user/$repo")
                )
            }
        }

        return Result.success(response)
    }

    override fun getRepositoriesPagerFlow(): Flow<PagingData<RepositoryDetailModel>> {
        logger.d("getRepositoriesPagerFlow()")

        return Pager(
            config = PagingConfig(PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = repositoriesRemoteMediator,
            pagingSourceFactory = { repositoriesPagingSource },
        ).flow
    }
}
