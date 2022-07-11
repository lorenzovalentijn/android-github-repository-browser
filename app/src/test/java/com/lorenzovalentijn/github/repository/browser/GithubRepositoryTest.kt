package com.lorenzovalentijn.github.repository.browser

import com.lorenzovalentijn.github.repository.data.api.PAGE_SIZE
import com.lorenzovalentijn.github.repository.data.db.RepositoryDatabase
import com.lorenzovalentijn.github.repository.data.mappers.RepositoryEntityMapper
import com.lorenzovalentijn.github.repository.data.repositories.github.GithubLocalDataSource
import com.lorenzovalentijn.github.repository.data.repositories.github.GithubRemoteDataSource
import com.lorenzovalentijn.github.repository.data.repositories.github.GithubRepositoryImpl
import com.lorenzovalentijn.github.repository.data.repositories.github.RepositoriesPagingSource
import com.lorenzovalentijn.github.repository.data.repositories.github.RepositoriesRemoteMediator
import com.lorenzovalentijn.github.repository.domain.AppLogger
import com.lorenzovalentijn.github.repository.domain.repositories.GithubRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class GithubRepositoryTest {

    private val logger: AppLogger = mockk()
    private val local: GithubLocalDataSource = mockk()
    private val remote: GithubRemoteDataSource = mockk()
    private val repositoryDatabase: RepositoryDatabase = mockk()
    private val repositoriesPagingSource: RepositoriesPagingSource = mockk()
    private val repositoriesRemoteMediator: RepositoriesRemoteMediator = mockk()
    private val repositoryEntityMapper: RepositoryEntityMapper = RepositoryEntityMapper()

    private val githubRepository: GithubRepository = GithubRepositoryImpl(
        logger,
        local,
        remote,
        repositoryDatabase,
        repositoriesPagingSource,
        repositoriesRemoteMediator,
        repositoryEntityMapper
    )

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Retrieve repositories from remote data source`() = runBlocking {
        coEvery { local.saveAll(any()) } just runs
        coEvery {
            remote.getRepositories("user", 1, PAGE_SIZE)
        } returns Result.success(expectRepositoryDetailModelList)

        val list = githubRepository.getRepositories("user", 1, PAGE_SIZE).getOrNull()
        assertContentEquals(list, expectRepositoryDetailModelList)
    }

    @Test
    fun `Fail to retrieve repositories from remote data source`() = runBlocking {
        val ex = Exception("No API data")
        coEvery { local.saveAll(any()) } just runs
        coEvery {
            remote.getRepositories("user", 1, PAGE_SIZE)
        } returns Result.failure(ex)

        val exception = githubRepository.getRepositories("user", 1, PAGE_SIZE).exceptionOrNull()
        assertEquals(exception, ex)
    }

    @Test
    fun `Retrieve repository details from remote data source`() = runBlocking {
        coEvery {
            remote.getRepositoryDetails("user", "repo")
        } returns Result.success(expectRepositoryDetailModel)

        val result = githubRepository.getRepositoryDetails("user", "repo")
        assertEquals(result.getOrThrow(), expectRepositoryDetailModel)
    }

    @Test
    fun `Fail to retrieve repository details from remote data source`() = runBlocking {
        val ex = Exception("No API data")
        coEvery {
            remote.getRepositoryDetails("user", "repo")
        } returns Result.failure(ex)
        every {
            repositoryDatabase.repositoryDao().findByFullName("user/repo")
        } returns expectRepositoryEntity

        val result = githubRepository.getRepositoryDetails("user", "repo")
        assertEquals(result.getOrThrow(), expectRepositoryDetailModel)
    }
}
