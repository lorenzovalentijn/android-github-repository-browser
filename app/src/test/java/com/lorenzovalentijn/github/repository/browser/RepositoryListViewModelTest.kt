
package com.lorenzovalentijn.github.repository.browser

import android.util.Log
import android.util.Log.d
import android.util.Log.e
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.flatMap
import androidx.paging.map
import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import com.lorenzovalentijn.github.repository.domain.AppLogger
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.repositories.GithubRepository
import com.lorenzovalentijn.github.repository.domain.usecases.GetRepositoryOverviewUseCase
import com.lorenzovalentijn.github.repository.presentation.DataState
import com.lorenzovalentijn.github.repository.presentation.viewmodels.RepositoryDetailsViewModel
import com.lorenzovalentijn.github.repository.presentation.viewmodels.RepositoryListViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.expect
import kotlin.time.Duration

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryListViewModelTest {

    private val githubRepository: GithubRepository = mockk()
    private val logger: AppLogger = TestLogger()
    private val getRepositoryListUseCase: GetRepositoryOverviewUseCase =
        GetRepositoryOverviewUseCase(githubRepository, logger, Dispatchers.Main)

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
        val viewModel = RepositoryListViewModel(logger, getRepositoryListUseCase)

        viewModel.state.test {
            assertEquals(
                DataState(isLoading = true, error = "no answer found for: GithubRepository(#3).getRepositoriesPagerFlow()"),
                awaitItemPrecededBy(DataState(isLoading = false))
            )
        }
    }

    // @Test
    // fun `Error in use case`() = runBlocking {
    //     every {
    //         githubRepository.getRepositoriesPagerFlow()
    //     } returns flow { throw Exception("FlowException") }
    //
    //     val viewModel = RepositoryListViewModel(logger, getRepositoryListUseCase)
    // }
    //
    // @Test
    // fun `Receive correct data`() = runBlocking {
    //     every {
    //         githubRepository.getRepositoriesPagerFlow()
    //     } returns flowOf(pagingData)
    //
    //     val viewModel = RepositoryListViewModel(logger, getRepositoryListUseCase)
    // }
}

// There's a race condition where intermediate states can get missed if the next state comes too fast.
// This function addresses that by awaiting an item that may or may not be preceded by the specified other items
suspend fun <T>FlowTurbine<DataState<T>>.awaitItemPrecededBy(vararg items: DataState<T>): DataState<T> {
    var nextItem = awaitItem()
    for (item in items) {
        if (item == nextItem) {
            nextItem = awaitItem()
        }
    }
    return nextItem
}
