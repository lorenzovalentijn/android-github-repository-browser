package com.lorenzovalentijn.github.repository.domain.models

data class RepositoryDetailModel(
    val id: Long,
    val name: String,
    val owner: String,
    val fullName: String,
    val description: String,
    val ownerAvatarUrl: String,
    val visibility: String,
    val isPrivate: Boolean,
    val htmlUrl: String,
)
