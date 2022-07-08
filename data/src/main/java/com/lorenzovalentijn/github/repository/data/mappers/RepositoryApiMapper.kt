package com.lorenzovalentijn.github.repository.data.mappers

import com.lorenzovalentijn.github.repository.data.api.GithubRepositoryModel
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel

class RepositoryApiMapper {
    fun toRepositoryDetailModelList(model: ArrayList<GithubRepositoryModel>): List<RepositoryDetailModel> {
        return model.map {
            toRepositoryDetailModel(it)
        }
    }

    fun toRepositoryDetailModel(model: GithubRepositoryModel): RepositoryDetailModel {
        return RepositoryDetailModel(
            name = model.name ?: "",
            owner = model.owner?.login ?: "",
            fullName = model.fullName ?: "",
            description = model.description ?: "",
            ownerAvatarUrl = model.owner?.avatarUrl ?: "",
            visibility = model.visibility ?: "",
            isPrivate = model.isPrivate ?: true,
            htmlUrl = model.htmlUrl ?: "",
        )
    }
}
