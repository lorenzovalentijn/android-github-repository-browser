package com.lorenzovalentijn.github.repository.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.lorenzovalentijn.github.repository.data.repositories.github.RepositoriesPagingSource
import com.lorenzovalentijn.github.repository.data.repositories.github.RepositoriesRemoteMediator
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.usecases.GetRepositoryOverviewUseCase
import com.lorenzovalentijn.github.repository.presentation.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
class RepositoryListViewModel(
    private val getRepositoryListUseCase: GetRepositoryOverviewUseCase,
    private val pager: RepositoriesPagingSource,
    private val remoteMediator: RepositoriesRemoteMediator,
): ViewModel() {

    private val mutableState: MutableStateFlow<DataState<List<RepositoryDetailModel>>> =
        MutableStateFlow(
            DataState(
                isLoading = true
            )
        )
    val state: StateFlow<DataState<List<RepositoryDetailModel>>> = mutableState

    init {
        refresh()
    }

    val pagingFlow = Pager(PagingConfig(10)) {
        pager
    }.flow.cachedIn(viewModelScope)

    val mediatorFlow = Pager(
        config = PagingConfig(10),
        remoteMediator = remoteMediator
    ) {
        pager
    }.flow
    .catch { throwable ->
        mutableState.update { dataState -> dataState.copy(error = throwable.message) }
    }
    .cachedIn(viewModelScope)

    fun refresh() {
        mutableState.update { it.copy(isLoading = true) }
        mutableState.update { it.copy(isLoading = false, data = emptyList()) }

        viewModelScope.launch {
            // TODO Add safety around use cases, this crashes without internet connection
            // try {
            //     val result = getRepositoryListUseCase()
            //     result.onSuccess { model ->
            //         mutableState.update {
            //             it.copy(
            //                 isLoading = false,
            //                 data = model
            //             )
            //         }
            //     }.onFailure { error ->
            //         mutableState.update {
            //             it.copy(
            //                 isLoading = false,
            //                 error = error.message
            //             )
            //         }
            //     }
            // } catch (e: Exception) {
            //     Log.e("LRV", "internet error", e)
            // }
        }
    }
}
