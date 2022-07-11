package com.lorenzovalentijn.github.repository.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "owner") val owner: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "owner_avatar_url") val ownerAvatarUrl: String?,
    @ColumnInfo(name = "visibility") val visibility: String?,
    @ColumnInfo(name = "is_private") val isPrivate: Boolean?,
    @ColumnInfo(name = "html_url") val htmlUrl: String?,
)
