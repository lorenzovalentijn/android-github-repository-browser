package com.lorenzovalentijn.github.repository.browser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.lorenzovalentijn.github.repository.browser.ui.theme.Black
import com.lorenzovalentijn.github.repository.browser.ui.theme.Yellow
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

    LaunchedEffect(user, repo) {
        viewModel.loadData(user, repo)
    }
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
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Repository Details") }, navigationIcon = {
                IconButton(onClick = { popBack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)) {
            Column(modifier = Modifier.fillMaxSize()) {
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
                    Box(modifier = Modifier.align(CenterHorizontally)) {
                        AsyncImage(
                            model = data.ownerAvatarUrl,
                            contentDescription = "Owner's avatar image",
                            modifier = Modifier
                                .size(128.dp)
                                .padding(10.dp),
                            error = rememberVectorPainter(image = Icons.Filled.Person),
                        )
                    }
                    Spacer(modifier = Modifier.size(32.dp))
                    Divider()
                    DetailItem(title = "Name", value = data.name)
                    DetailItem(title = "Full name", value = data.fullName)
                    DetailItem(title = "Description", value = data.description)
                    DetailItem(title = "Visibility", value = data.visibility)
                    DetailItem(title = "Private", value = data.isPrivate.toString())
                    Spacer(modifier = Modifier.size(32.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Yellow),
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.htmlUrl)))
                        }
                    ) {
                        Text(text = "Open in external browser", color = Black)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(title: String, value: String) {
    Row {
        Column(modifier = Modifier.width(100.dp)) {
            Text(title)
        }
        Column {
            Text(value)
        }
    }
    Divider()
}

@Preview
@Composable
fun RepositoryDetailsScreenContentPreview() {
    RepositoryDetailsScreenContent(
        DataState(
            data = RepositoryDetailModel(
                id = 0,
                name = "airflow",
                owner = "abnamrocoesd",
                fullName = "abnamrocoesd/airflow",
                description = "Apache Airflow - A platform to programmatically author, schedule, and monitor workflows",
                ownerAvatarUrl = "https://avatars.githubusercontent.com/u/15876397?v=4",
                visibility = "public",
                isPrivate = false,
                htmlUrl = "https://github.com/abnamrocoesd/airflow"
            )
        )
    )
}
