package com.lorenzovalentijn.github.repository.presentation

data class DataState<out T>(
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val error: String? = null,
    val data: T? = null,
)
