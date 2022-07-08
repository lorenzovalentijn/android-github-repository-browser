package com.lorenzovalentijn.github.repository.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lorenzovalentijn.github.repository.data.entities.RepositoryEntity

@Database(entities = [RepositoryEntity::class], version = 1, exportSchema = false)
abstract class RepositoryDatabase: RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao

    companion object {
        fun getDatabase(context: Context): RepositoryDatabase {
            return Room.databaseBuilder(
                context,
                RepositoryDatabase::class.java,
                "database-repository"
            ).build()
        }
    }
}
