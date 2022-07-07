package com.lorenzovalentijn.github.repository.browser.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lorenzovalentijn.github.repository.browser.Greeting
import com.lorenzovalentijn.github.repository.browser.NewComponent
import org.koin.androidx.compose.get

@Composable
fun RepositoryDetailsScreen(
    popBack: () -> Unit = {},
    ) {
    RepositoryDetailsScreenContent(popBack = { popBack() })
}

@Composable
fun RepositoryDetailsScreenContent(
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
        Column(modifier = Modifier.fillMaxSize()) {
            val bbb: NewComponent = get()
            Greeting(bbb.name)
        }
    }
}

@Preview
@Composable
fun RepositoryDetailsScreenContentPreview() {
    RepositoryDetailsScreenContent()
}
