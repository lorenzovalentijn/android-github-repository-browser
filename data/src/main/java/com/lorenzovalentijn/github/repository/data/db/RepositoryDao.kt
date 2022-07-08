package com.lorenzovalentijn.github.repository.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lorenzovalentijn.github.repository.data.entities.RepositoryEntity

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM repositories")
    fun getAll(): List<RepositoryEntity>

    @Query("SELECT * FROM repositories WHERE full_name LIKE :fullName LIMIT 1")
    fun findByFullName(fullName: String): RepositoryEntity

    @Query("SELECT * FROM repositories WHERE name LIKE :name LIMIT 1")
    fun findByRepoName(name: String): RepositoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg repositories: RepositoryEntity)

    @Delete
    fun delete(user: RepositoryEntity)
}
