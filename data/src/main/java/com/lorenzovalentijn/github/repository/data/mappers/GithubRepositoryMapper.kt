package com.lorenzovalentijn.github.repository.data.mappers

import com.lorenzovalentijn.github.repository.data.models.GithubRepositoryModel
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel

class GithubRepositoryMapper {
    fun toRepositoryModelList(model: ArrayList<GithubRepositoryModel>): List<RepositoryModel> {
        return model.map {
            toRepositoryModel(it)
        }
    }

    private fun toRepositoryModel(model: GithubRepositoryModel): RepositoryModel {
        return RepositoryModel(
            name = model.name ?: "",
            ownerAvatarUrl = model.owner?.avatarUrl ?: "",
            visibility = model.visibility ?: "",
            isPrivate = model.isPrivate ?: true,
        )
    }

    fun toRepositoryDetailModel(model: GithubRepositoryModel): RepositoryDetailModel {
        return RepositoryDetailModel(
            name = model.name ?: "",
            fullName = model.fullName ?: "",
            description = model.description ?: "",
            ownerAvatarUrl = model.owner?.avatarUrl ?: "",
            visibility = model.visibility ?: "",
            isPrivate = model.isPrivate ?: true,
            htmlUrl = model.htmlUrl ?: "",
        )
    }
}
