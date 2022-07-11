package com.lorenzovalentijn.github.repository.browser

import androidx.paging.PagingData
import com.lorenzovalentijn.github.repository.data.api.GithubRepositoryModel
import com.lorenzovalentijn.github.repository.data.entities.RepositoryEntity
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import kotlinx.coroutines.flow.flowOf

val expectRepositoryDetailModel = RepositoryDetailModel(
    id = 1,
    name = "Repo Lorenzo",
    owner = "lorenzovalentijn",
    fullName = "lorenzovalentijn/repo",
    description = "Full Repository Description",
    ownerAvatarUrl = "https://avatars.githubusercontent.com/u/11546716?v=4",
    visibility = "public",
    isPrivate = false,
    htmlUrl = "lorenzovalentijn.com",
)

val expectRepositoryDetailModelList = listOf(
    expectRepositoryDetailModel,
    RepositoryDetailModel(
        2,
        "Repo 2",
        "lorenzovalentijn",
        "lorenzovalentijn/repo",
        "Full Repository Description",
        "",
        "public",
        true,
        "",
    ),
    RepositoryDetailModel(
        3,
        "Repo 3",
        "lorenzovalentijn",
        "lorenzovalentijn/repo",
        "Full Repository Description",
        "", "public", true,
        "",
    ),
    RepositoryDetailModel(
        4,
        "Repo 4",
        "lorenzovalentijn",
        "lorenzovalentijn/repo",
        "Full Repository Description",
        "",
        "public",
        true,
        "",
    ),
)
val pagingData = PagingData.from(expectRepositoryDetailModelList)
val flowOfPagingData = flowOf(pagingData)

val expectRepositoryEntity = RepositoryEntity(
    id = 1,
    fullName = "lorenzovalentijn/repo",
    name = "Repo Lorenzo",
    owner = "lorenzovalentijn",
    description = "Full Repository Description",
    ownerAvatarUrl = "https://avatars.githubusercontent.com/u/11546716?v=4",
    visibility = "public",
    isPrivate = false,
    htmlUrl = "lorenzovalentijn.com",
)

val expectGithubRepositoryModel = GithubRepositoryModel(
    id = 1,
    name = "Repo Lorenzo",
    owner = GithubRepositoryModel.Owner(
        login = "lorenzovalentijn",
        avatarUrl = "https://avatars.githubusercontent.com/u/11546716?v=4"
    ),
    fullName = "lorenzovalentijn/repo",
    description = "Full Repository Description",
    visibility = "public",
    isPrivate = false,
    htmlUrl = "lorenzovalentijn.com",
)
