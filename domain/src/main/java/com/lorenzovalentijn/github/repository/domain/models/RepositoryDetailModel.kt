package com.lorenzovalentijn.github.repository.domain.models

class RepositoryDetailModel(
    name: String,
    val fullName: String,
    val description: String,
    ownerAvatarUrl: String,
    visibility: String,
    isPrivate: Boolean,
    val htmlUrl: String,
) : RepositoryModel(name, ownerAvatarUrl, visibility, isPrivate)
