package com.lorenzovalentijn.github.repository.data.repositories.github

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.lorenzovalentijn.github.repository.data.db.RepositoryDatabase
import com.lorenzovalentijn.github.repository.data.mappers.RepositoryEntityMapper
import com.lorenzovalentijn.github.repository.domain.AppLogger
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RepositoriesRemoteMediator(
    private val logger: AppLogger,
    private val user: String,
    private val repositoryDatabase: RepositoryDatabase,
    private val githubRemoteDataSource: GithubRemoteDataSource,
    private val repositoryEntityMapper: RepositoryEntityMapper,
) : RemoteMediator<Int, RepositoryDetailModel>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepositoryDetailModel>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.fullName
                }
            }
            val page = state.anchorPosition ?: 1
            logger.d("page: $page")
            logger.d("state.anchorPosition: ${state.anchorPosition}")
            val response = githubRemoteDataSource.getRepositories(user, page)
            repositoryDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // repositoryDatabase.repositoryDao().clearAll()
                    logger.d("Clear all data.")
                }

                repositoryDatabase.repositoryDao().insertAll(
                    response.getOrThrow().map {
                        repositoryEntityMapper.toRepositoryEntity(it, state.anchorPosition?.toString())
                    }
                )
            }

            MediatorResult.Success(endOfPaginationReached = (loadKey == null))
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
