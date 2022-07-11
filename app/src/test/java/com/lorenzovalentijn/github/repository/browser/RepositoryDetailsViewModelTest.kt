package com.lorenzovalentijn.github.repository.browser

import app.cash.turbine.test
import com.lorenzovalentijn.github.repository.domain.AppLogger
import com.lorenzovalentijn.github.repository.domain.repositories.GithubRepository
import com.lorenzovalentijn.github.repository.domain.usecases.GetRepositoryDetailsUseCase
import com.lorenzovalentijn.github.repository.presentation.DataState
import com.lorenzovalentijn.github.repository.presentation.viewmodels.RepositoryDetailsViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryDetailsViewModelTest {

    private val logger: AppLogger = mockk()
    private val githubRepository: GithubRepository = mockk()
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase =
        GetRepositoryDetailsUseCase(githubRepository, logger, Dispatchers.Main)

    private val viewModel = RepositoryDetailsViewModel(getRepositoryDetailsUseCase)

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Show loading state on init`() = runBlocking {
        viewModel.state.test {
            assertEquals(
                DataState(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )
        }
    }

    @Test
    fun `Do not supply user name`() = runBlocking {
        viewModel.state.test {
            assertEquals(
                DataState(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )

            expectNoEvents()
            viewModel.loadData(null, "repo").join()

            assertEquals(
                DataState(error = "User Name or Repository Name is missing."),
                awaitItemPrecededBy(DataState(isLoading = true))
            )
        }
    }

    @Test
    fun `Do not supply repo name`() = runBlocking {
        viewModel.state.test {
            assertEquals(
                DataState(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )

            expectNoEvents()
            viewModel.loadData("user", null).join()

            assertEquals(
                DataState(error = "User Name or Repository Name is missing."),
                awaitItemPrecededBy(DataState(isLoading = true))
            )
        }
    }

    @Test
    fun `Get correct Repository Details`() = runBlocking {
        coEvery {
            githubRepository.getRepositoryDetails("user", "repo")
        } returns Result.success(expectRepositoryDetailModel)

        viewModel.state.test {
            assertEquals(
                DataState(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )
            expectNoEvents()
            viewModel.loadData("user", "repo").join()

            assertEquals(
                DataState(data = expectRepositoryDetailModel),
                awaitItemPrecededBy(DataState(isLoading = true))
            )
        }
    }

    @Test
    fun `Error in API`() = runBlocking {
        every { logger.e(any(), any()) } just runs
        coEvery {
            githubRepository.getRepositoryDetails("user", "repo")
        } returns Result.failure(Exception("TestErrorInAPI"))

        viewModel.state.test {
            assertEquals(
                DataState(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )
            expectNoEvents()
            viewModel.loadData("user", "repo").join()

            assertEquals(
                DataState(error = "TestErrorInAPI"),
                awaitItemPrecededBy(DataState(isLoading = true))
            )
        }
    }
}
