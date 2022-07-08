package com.lorenzovalentijn.github.repository.browser.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.lorenzovalentijn.github.repository.domain.models.RepositoryDetailModel
import com.lorenzovalentijn.github.repository.presentation.DataState
import com.lorenzovalentijn.github.repository.presentation.viewmodels.RepositoryDetailsViewModel
import org.koin.androidx.compose.getViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RepositoryDetailsScreen(
    user: String?,
    repo: String?,
    viewModel: RepositoryDetailsViewModel = getViewModel(),
    popBack: () -> Unit = {},
    ) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifeCycleAwareStateFlow = remember(viewModel.state, lifecycleOwner) {
        viewModel.state.flowWithLifecycle(lifecycleOwner.lifecycle)
    }
    val state by lifeCycleAwareStateFlow.collectAsState(initial = viewModel.state.value)

    viewModel.refresh(user, repo)
    RepositoryDetailsScreenContent(
        state = state,
        popBack = { popBack() },
    )
}

@Composable
fun RepositoryDetailsScreenContent(
    state: DataState<RepositoryDetailModel>,
    popBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Repository Details") }, navigationIcon = {
                IconButton(onClick = { popBack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Center))
            }
        }
        if (state.isEmpty) {
            EmptyText("Repository List is empty.")
        }
        state.error?.let {
            ErrorText(it)
        }
        state.data?.let { data ->
            Box {
                AsyncImage(
                    // model = rememberAsyncImagePainter(model = repositoryModel.ownerAvatarUrl),
                    model = data.ownerAvatarUrl,
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
                Text(data.name)
                Text(data.visibility)
                Text(data.isPrivate.toString())
            }
        }
    }
}

@Preview
@Composable
fun RepositoryDetailsScreenContentPreview() {
    // TODO Enable previews
    // RepositoryDetailsScreenContent()
}
