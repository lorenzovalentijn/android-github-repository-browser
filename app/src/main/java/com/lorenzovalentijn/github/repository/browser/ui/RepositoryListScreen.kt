package com.lorenzovalentijn.github.repository.browser.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.lorenzovalentijn.github.repository.presentation.DataState

@Composable
fun RepositoryListScreen(
    navigate: () -> Unit = {},
) {
    val state = DataState<List<String>>(isLoading = false, isEmpty = false, data = listOf("1", "2", "3", "4"), error = "Error occured")
    RepositoryListScreenContent(
        state = state,
        navigate = { navigate() },
        onRefresh = {}
    )
}

@Composable
fun RepositoryListScreenContent(
    state: DataState<List<String>>,
    navigate: () -> Unit = {},
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
                        items(data) {
                            Row(
                                Modifier
                                    .fillMaxSize()
                                    .clickable { navigate() }
                                    .padding(10.dp)
                            ) {
                                Text("Item $it")
                            }
                            Divider()
                        }
                    }
                }
            }
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
fun RepositoryListScreenContentPreview() {
    RepositoryListScreenContent(DataState<List<String>>(isLoading = true, isEmpty = true, data = listOf("1", "2", "3", "4")))
}
