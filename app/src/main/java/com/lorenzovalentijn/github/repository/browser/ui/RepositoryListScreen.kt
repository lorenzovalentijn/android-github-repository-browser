package com.lorenzovalentijn.github.repository.browser.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.lorenzovalentijn.github.repository.browser.R
import com.lorenzovalentijn.github.repository.domain.models.RepositoryModel
import com.lorenzovalentijn.github.repository.presentation.DataState
import com.lorenzovalentijn.github.repository.presentation.viewmodels.RepositoryListViewModel
import org.koin.androidx.compose.getViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RepositoryListScreen(
    viewModel: RepositoryListViewModel = getViewModel(),
    navigate: (user: String, repo: String) -> Unit = {_, _ -> },
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifeCycleAwareStateFlow = remember(viewModel.state, lifecycleOwner) {
        viewModel.state.flowWithLifecycle(lifecycleOwner.lifecycle)
    }
    val state by lifeCycleAwareStateFlow.collectAsState(initial = viewModel.state.value)

    RepositoryListScreenContent(
        state = state,
        navigate = navigate,
        onRefresh = { viewModel.refresh() }
    )
}

@Composable
fun RepositoryListScreenContent(
    state: DataState<List<RepositoryModel>>,
    navigate: (user: String, repo: String) -> Unit = {_, _ -> },
    onRefresh: () -> Unit = {},
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Repository List") }) }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = state.isLoading),
                onRefresh = onRefresh
            ) {
                if (state.isEmpty) {
                    EmptyText("Repository List is empty.")
                }
                state.error?.let {
                    ErrorText(it)
                }
                state.data?.let { data ->
                    LazyColumn {
                        items(data) { repositoryModel ->
                            RepositoryRow(
                                repositoryModel = repositoryModel,
                                navigate = navigate
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RepositoryRow(
    repositoryModel: RepositoryModel,
    navigate: (user: String, repo: String) -> Unit = {_, _ -> },
    ) {
    Row(
        Modifier
            .fillMaxSize()
            .clickable { navigate(repositoryModel.owner, repositoryModel.name) }
            .padding(10.dp)
    ) {
        Box {
            AsyncImage(
                // model = rememberAsyncImagePainter(model = repositoryModel.ownerAvatarUrl),
                model = repositoryModel.ownerAvatarUrl,
                contentDescription = "Owner's avatar image",
                modifier = Modifier
                    .size(64.dp)
                    .padding(10.dp),
                error = rememberVectorPainter(image = Icons.Filled.Person),
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        Column {
            Text("Name")
            Text("Visibility")
            Text("Private")
        }
        Spacer(modifier = Modifier.size(10.dp))
        Column {
            Text(repositoryModel.name)
            Text(repositoryModel.visibility)
            Text(repositoryModel.isPrivate.toString())
        }
    }
}

@Composable
fun EmptyText(text: String = "") {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text)
    }
}

@Composable
fun ErrorText(error: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = error, color = Color.Red)
    }
}

@Preview
@Composable
fun RepositoryListScreenContentPreview_Loading() {
    RepositoryListScreenContent(DataState(isLoading = true))
}

@Preview
@Composable
fun RepositoryListScreenContentPreview_Empty() {
    RepositoryListScreenContent(DataState(isEmpty = true))
}

@Preview
@Composable
fun RepositoryListScreenContentPreview_Error() {
    RepositoryListScreenContent(DataState(error = "Data couldn't be loaded."))
}

@Preview
@Composable
fun RepositoryListScreenContentPreview_Success() {
    RepositoryListScreenContent(
        DataState(
            data = listOf(
                RepositoryModel(
                    "Repo Lorenzo",
                    "lorenzovalentijn",
                    "https://avatars.githubusercontent.com/u/11546716?v=4",
                    "public",
                    false
                ),
                RepositoryModel("Repo 2", "", "", "public", true),
                RepositoryModel("Repo 3", "", "", "public", true),
                RepositoryModel("Repo 4", "", "", "public", true),
            )
        )
    )
}
