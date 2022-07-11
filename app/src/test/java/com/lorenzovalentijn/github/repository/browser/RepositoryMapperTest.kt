package com.lorenzovalentijn.github.repository.browser

import com.lorenzovalentijn.github.repository.data.mappers.RepositoryApiMapper
import com.lorenzovalentijn.github.repository.data.mappers.RepositoryEntityMapper
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class RepositoryMapperTest {

    private val repositoryApiMapper = RepositoryApiMapper()
    private val repositoryEntityMapper = RepositoryEntityMapper()

    @Test
    fun `Map api model to domain model`() = runBlocking {
        val mappedModel = repositoryApiMapper.toRepositoryDetailModel(expectGithubRepositoryModel)
        assertEquals(mappedModel, expectRepositoryDetailModel)
    }

    @Test
    fun `Map api model to entity model`() = runBlocking {
        val mappedModel = repositoryEntityMapper.toRepositoryEntity(expectGithubRepositoryModel)
        assertEquals(mappedModel, expectRepositoryEntity)
    }

    @Test
    fun `Map entity model to domain model`() = runBlocking {
        val mappedModel = repositoryEntityMapper.toRepositoryDetailModel(expectRepositoryEntity)
        assertEquals(mappedModel, expectRepositoryDetailModel)
    }

    @Test
    fun `Map domain model to entity model`() = runBlocking {
        val mappedModel = repositoryEntityMapper.toRepositoryEntity(expectRepositoryDetailModel)
        assertEquals(mappedModel, expectRepositoryEntity)
    }
}
