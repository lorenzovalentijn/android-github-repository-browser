package com.lorenzovalentijn.github.repository.data.repositories.github

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.lorenzovalentijn.github.repository.data.api.START_PAGE
import com.lorenzovalentijn.github.repository.data.db.RepositoryDatabase
import com.lorenzovalentijn.github.repository.data.entities.RemoteKeysEntity
import com.lorenzovalentijn.github.repository.data.entities.RepositoryEntity
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
            logger.d("loadType: $loadType")
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    logger.d("Page.REFRESH: ${remoteKeys?.nextKey?.minus(1)}")
                    remoteKeys?.nextKey?.minus(1) ?: START_PAGE
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    logger.d("Page.PREPEND: $prevKey")
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    logger.d("Page.APPEND: $nextKey")
                    nextKey
                }
            }
            logger.d("loadKey: $page")
            logger.d("CALL RepositoriesRemoteMediator.getRepositories($user, $page, ${state.config.pageSize})")
            val response = githubRemoteDataSource.getRepositories(user, page, state.config.pageSize)
            logger.d("RETURN RepositoriesRemoteMediator.getRepositories($user, $page, ${state.config.pageSize})")
            val repos = response.getOrThrow()
            logger.d("REPO.RepositoriesRemoteMediator = $repos")
            val endOfPaginationReached = repos.isEmpty()
            repositoryDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    logger.d("Clear all data.")
                    repositoryDatabase.remoteKeysDao().clearRemoteKeys()
                    repositoryDatabase.repositoryDao().clearAll()
                }
                val prevKey = if (page == START_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = repos.map {
                    RemoteKeysEntity(
                        repoId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                    )
                }
                val repoEntities = repos.map {
                    repositoryEntityMapper.toRepositoryEntity(it)
                }

                repositoryDatabase.remoteKeysDao().insertAll(keys)
                repositoryDatabase.repositoryDao().insertAll(repoEntities)
                logger.d("Inserted ${keys.size} keys: $keys")
                logger.d("Inserted ${repos.size} repos: $repos")
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            logger.e("IOException for network failures.", e)
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            logger.e("HttpException for any non-2xx HTTP status codes.", e)
            MediatorResult.Error(e)
        } catch (e: Exception) {
            logger.e("Error in loading RepositoriesPagingSource.", e)
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RepositoryDetailModel>): RemoteKeysEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                repositoryDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RepositoryDetailModel>): RemoteKeysEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                repositoryDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, RepositoryDetailModel>
    ): RemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                repositoryDatabase.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }
}
