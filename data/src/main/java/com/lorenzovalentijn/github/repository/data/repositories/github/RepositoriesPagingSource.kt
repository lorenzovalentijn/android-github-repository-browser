package com.lorenzovalentijn.github.repository.data.repositories.github

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lorenzovalentijn.github.repository.data.api.PAGE_SIZE
import com.lorenzovalentijn.github.repository.data.api.START_PAGE
import com.lorenzovalentijn.github.repository.data.db.RepositoryDatabase
import com.lorenzovalentijn.github.repository.data.mappers.RepositoryEntityMapper
import com.lorenzovalentijn.github.repository.domain.AppLogger
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RepositoriesPagingSource(
    val user: String,
    private val repositoryDatabase: RepositoryDatabase,
    private val githubRemoteDataSource: GithubRemoteDataSource,
    private val repositoryEntityMapper: RepositoryEntityMapper,
    private val logger: AppLogger,
    ) : PagingSource<Int, RepositoryDetailModel>() {

    override fun getRefreshKey(state: PagingState<Int, RepositoryDetailModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryDetailModel> {
        return try {
            val page = params.key ?: START_PAGE

            val repos = getReposFromApi(user, page, params.loadSize)
                ?: getReposFromDb()
                ?: emptyList()

            // val response = githubRemoteDataSource.getRepositories(user, page, params.loadSize)
            // val repos = response.getOrDefault(emptyList())
            logger.d("Retrieved ${repos.size} repos = $repos")
            val prevKey = if (page == START_PAGE) null else page - 1
            val nextKey = if (repos.isEmpty()) null else page + (params.loadSize / PAGE_SIZE)
            logger.d("page: $page, prevKey: $prevKey, nextKey: $nextKey")

            LoadResult.Page(
                data = repos,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            logger.e("IOException for network failures.", e)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            logger.e("HttpException for any non-2xx HTTP status codes.", e)
            LoadResult.Error(e)
        } catch (e: Exception) {
            logger.e("Error in loading RepositoriesPagingSource.", e)
            LoadResult.Error(e)
        }
    }

    private suspend fun getReposFromDb(): List<RepositoryDetailModel>? {
        return withContext(Dispatchers.IO) {
            repositoryDatabase.repositoryDao().selectAll().map {
                repositoryEntityMapper.toRepositoryDetailModel(it)
            }
        }
    }

    private suspend fun getReposFromApi(user: String, page: Int, perPage: Int): List<RepositoryDetailModel>? {
        return try {
            val response = githubRemoteDataSource.getRepositories(user, page, perPage)
            response.getOrNull()
        } catch (e: Exception) {
            logger.e("Error retrieving repos from the API.", e)
            null
        }
    }
}
