package com.lorenzovalentijn.github.repository.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lorenzovalentijn.github.repository.domain.AppLogger
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.usecases.GetRepositoryOverviewUseCase
import com.lorenzovalentijn.github.repository.presentation.DataState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepositoryListViewModel(
    private val logger: AppLogger,
    private val getRepositoryListUseCase: GetRepositoryOverviewUseCase,
) : ViewModel() {

    private val mutableState: MutableStateFlow<DataState<List<RepositoryDetailModel>>> =
        MutableStateFlow(
            DataState(
                isLoading = true
            )
        )
    val state: StateFlow<DataState<List<RepositoryDetailModel>>> = mutableState
    var pagingResult: Flow<PagingData<RepositoryDetailModel>> = emptyFlow()

    init {
        loadData()
    }

    fun loadData(): Job {
        mutableState.update { it.copy(isLoading = true) }

        return viewModelScope.launch {
            try {
                pagingResult = getRepositoryListUseCase(Any())
                    .getOrThrow()
                    .catch { throwable ->
                        mutableState.update { dataState -> dataState.copy(error = throwable.message) }
                    }.cachedIn(viewModelScope)
                mutableState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                logger.e("Error in paging result.", e)
                mutableState.update { dataState -> dataState.copy(error = e.message) }
            }
        }
    }
}
