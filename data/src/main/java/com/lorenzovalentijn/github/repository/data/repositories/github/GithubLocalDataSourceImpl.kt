package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.data.db.RepositoryDao
import com.lorenzovalentijn.github.repository.data.db.RepositoryDatabase
import com.lorenzovalentijn.github.repository.data.mappers.RepositoryEntityMapper
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GithubLocalDataSourceImpl(
    private val repositoryDatabase: RepositoryDatabase,
    private val repositoryEntityMapper: RepositoryEntityMapper,
    private val coroutineDispatcher: CoroutineDispatcher,
) : GithubLocalDataSource {

    private val repositoryDao: RepositoryDao = repositoryDatabase.repositoryDao()

    override suspend fun getRepositories(): Flow<List<RepositoryDetailModel>> {
        val repositoriesFlow = repositoryDao.getAll()
        return repositoriesFlow.map { list ->
            repositoryEntityMapper.toRepositoryDetailModelList(list)
        }
    }

    override suspend fun saveAll(repositories: List<RepositoryDetailModel>) {
        withContext(coroutineDispatcher) {
            repositoryDao.insertAll(repositoryEntityMapper.toRepositoryEntityList(repositories))
        }
    }
}
