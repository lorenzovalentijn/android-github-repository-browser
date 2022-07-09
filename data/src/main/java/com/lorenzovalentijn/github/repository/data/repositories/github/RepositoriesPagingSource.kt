package com.lorenzovalentijn.github.repository.data.repositories.github

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import retrofit2.HttpException
import java.io.IOException

class RepositoriesPagingSource(
    val user: String,
    private val githubRemoteDataSource: GithubRemoteDataSource,
) : PagingSource<Int, RepositoryDetailModel>() {

    override fun getRefreshKey(state: PagingState<Int, RepositoryDetailModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryDetailModel> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = githubRemoteDataSource.getRepositories(user, nextPageNumber)
            LoadResult.Page(
                data = response.getOrThrow(),
                prevKey = null,
                nextKey = nextPageNumber + 1
            )
        } catch (e: IOException) {
            // IOException for network failures.
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
