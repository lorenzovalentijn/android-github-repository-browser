package com.lorenzovalentijn.github.repository.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel
import com.lorenzovalentijn.github.repository.domain.usecases.GetRepositoryListUseCase
import com.lorenzovalentijn.github.repository.presentation.DataState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepositoryListViewModel(
    private val getRepositoryListUseCase: GetRepositoryListUseCase
): ViewModel() {

    private val mutableState: MutableStateFlow<DataState<List<RepositoryModel>>> =
        MutableStateFlow(
            DataState(
                isLoading = true,
                data = emptyList()
            )
        )
    val state: StateFlow<DataState<List<RepositoryModel>>> = mutableState

    init {
        refresh()
    }

    fun refresh() {
        mutableState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val repositoryList = getRepositoryListUseCase.execute()
            mutableState.update {
                it.copy(
                    isLoading = false,
                    data = repositoryList
                )
            }
        }
    }
}
