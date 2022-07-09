package com.lorenzovalentijn.github.repository.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lorenzovalentijn.github.repository.data.entities.RepositoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM repositories")
    fun getAll(): Flow<List<RepositoryEntity>>

    @Query("SELECT * FROM repositories WHERE full_name LIKE :fullName LIMIT 1")
    fun findByFullName(fullName: String): RepositoryEntity

    @Query("SELECT * FROM repositories WHERE name LIKE :name LIMIT 1")
    fun findByRepoName(name: String): RepositoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg repositories: RepositoryEntity)

    @Delete
    fun delete(user: RepositoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repositories: List<RepositoryEntity>)

    // TODO WHERE DO WE NEED THIS?
    @Query("SELECT * FROM repositories WHERE label LIKE :query")
    fun pagingSource(query: String): PagingSource<Int, RepositoryEntity>

    @Query("DELETE FROM repositories")
    suspend fun clearAll()
}
