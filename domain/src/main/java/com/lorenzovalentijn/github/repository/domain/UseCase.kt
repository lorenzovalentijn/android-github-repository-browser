package com.lorenzovalentijn.github.repository.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCase<in Params, Return>(
    private val logger: AppLogger,
    private val coroutineDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(parameters: Params): Result<Return> {
        return try {
            withContext(coroutineDispatcher) {
                execute(parameters).let {
                    Result.success(it)
                }
            }
        } catch (e: Exception) {
            logger.e("Error in UseCase.", e)
            Result.failure(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: Params): Return
}
