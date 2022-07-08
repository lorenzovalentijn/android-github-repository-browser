package com.lorenzovalentijn.github.repository.domain.models

open class RepositoryModel(
    val name: String,
    val ownerAvatarUrl: String,
    val visibility: String,
    val isPrivate: Boolean,
)
