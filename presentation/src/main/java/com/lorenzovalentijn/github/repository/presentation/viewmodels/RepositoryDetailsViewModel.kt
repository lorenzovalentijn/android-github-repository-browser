package com.lorenzovalentijn.github.repository.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.usecases.GetRepositoryDetailsUseCase
import com.lorenzovalentijn.github.repository.presentation.DataState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepositoryDetailsViewModel(
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<DataState<RepositoryDetailModel>> =
        MutableStateFlow(
            DataState(
                isLoading = true,
            )
        )
    val state: StateFlow<DataState<RepositoryDetailModel>> = mutableState

    fun refresh(user: String?, repo: String?) {

        if (user == null || repo == null) {
            mutableState.update {
                it.copy(
                    isLoading = false,
                    error = "User Name or Repository Name is missing."
                )
            }
            return
        }

        mutableState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val repositoryDetails = getRepositoryDetailsUseCase.execute(user, repo)
            mutableState.update {
                it.copy(
                    isLoading = false,
                    data = repositoryDetails
                )
            }
        }
    }
}
