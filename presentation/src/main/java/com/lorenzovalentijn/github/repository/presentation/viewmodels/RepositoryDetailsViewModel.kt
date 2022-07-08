package com.lorenzovalentijn.github.repository.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.usecases.GetRepositoryDetailsUseCase
import com.lorenzovalentijn.github.repository.presentation.DataState
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
                isLoading = true
            )
        )
    val state: StateFlow<DataState<RepositoryDetailModel>> = mutableState

    fun refresh(user: String?, repo: String?) {
        mutableState.update { it.copy(isLoading = true) }

        if (user == null || repo == null) {
            mutableState.update {
                it.copy(
                    isLoading = false,
                    error = "User Name or Repository Name is missing."
                )
            }
            return
        }

        viewModelScope.launch {
            val result = getRepositoryDetailsUseCase(user, repo)
            result.onSuccess { model ->
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        data = model
                    )
                }
            }.onFailure { error ->
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }
        }
    }
}
