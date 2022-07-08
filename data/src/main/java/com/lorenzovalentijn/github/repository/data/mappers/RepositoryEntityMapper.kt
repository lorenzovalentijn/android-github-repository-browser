package com.lorenzovalentijn.github.repository.data.mappers

import com.lorenzovalentijn.github.repository.data.entities.RepositoryEntity
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel

class RepositoryEntityMapper {
    fun toRepositoryDetailModelList(model: List<RepositoryEntity>): List<RepositoryDetailModel> {
        return model.map {
            toRepositoryDetailModel(it)
        }
    }

    fun toRepositoryDetailModel(model: RepositoryEntity): RepositoryDetailModel {
        return RepositoryDetailModel(
            name = model.name ?: "",
            owner = model.owner ?: "",
            fullName = model.fullName ?: "",
            description = model.description ?: "",
            ownerAvatarUrl = model.ownerAvatarUrl ?: "",
            visibility = model.visibility ?: "",
            isPrivate = model.isPrivate ?: true,
            htmlUrl = model.htmlUrl ?: "",
        )
    }

    fun toRepositoryEntityList(model: List<RepositoryDetailModel>): List<RepositoryEntity> {
        return model.map {
            toRepositoryEntity(it)
        }
    }

    fun toRepositoryEntity(model: RepositoryDetailModel): RepositoryEntity {
        return RepositoryEntity(
            name = model.name,
            owner = model.owner,
            fullName = model.fullName,
            description = model.description,
            ownerAvatarUrl = model.ownerAvatarUrl,
            visibility = model.visibility,
            isPrivate = model.isPrivate,
            htmlUrl = model.htmlUrl,
        )
    }
}
