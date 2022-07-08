package com.lorenzovalentijn.github.repository.data.repositories.github

import com.lorenzovalentijn.github.repository.data.db.RepositoryDao
import com.lorenzovalentijn.github.repository.data.db.RepositoryDatabase
import com.lorenzovalentijn.github.repository.data.mappers.RepositoryEntityMapper
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GithubLocalDataSourceImpl(
    private val repositoryDatabase: RepositoryDatabase,
    private val repositoryEntityMapper: RepositoryEntityMapper,
    private val coroutineDispatcher: CoroutineDispatcher,
) : GithubLocalDataSource {

    private val repositoryDao: RepositoryDao = repositoryDatabase.repositoryDao()

    override suspend fun getRepositories(): List<RepositoryDetailModel> {
        return repositoryEntityMapper.toRepositoryDetailModelList(repositoryDao.getAll())
    }

    override suspend fun saveAll(repositories: List<RepositoryDetailModel>) {
        withContext(coroutineDispatcher) {
            repositoryDao.insertAll(*repositoryEntityMapper.toRepositoryEntityList(repositories).toTypedArray())
        }
    }
}
