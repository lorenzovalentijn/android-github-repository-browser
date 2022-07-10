package com.lorenzovalentijn.github.repository.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in Params, Return>(
    private val logger: AppLogger,
    private val coroutineDispatcher: CoroutineDispatcher,
) {
    operator fun invoke(parameters: Params): Flow<Result<Return>> =
        execute(parameters)
            .catch {
                logger.e("Error in FlowUseCase.", it)
                emit(Result.failure(it))
            }
            .flowOn(coroutineDispatcher)

    protected abstract fun execute(parameters: Params): Flow<Result<Return>>
}
