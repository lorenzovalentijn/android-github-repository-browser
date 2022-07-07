package com.lorenzovalentijn.github.repository.browser

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.lorenzovalentijn.github.repository.browser.ui.RepositoryDetailsScreen
import com.lorenzovalentijn.github.repository.browser.ui.RepositoryListScreen
import com.lorenzovalentijn.github.repository.browser.ui.theme.GithubRepositoryBrowserTheme

sealed class Screen(val name: String) {
    object RepositoryListScreen : Screen("RepositoryListScreen")
    object RepositoryDetailsScreen : Screen("RepositoryDetailsScreen")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavController() {
    val navController = rememberAnimatedNavController()
    val enterTransition = slideInHorizontally() + fadeIn(tween(1_000))
    val exitTransition = slideOutHorizontally() + fadeOut(tween(1_000))

    GithubRepositoryBrowserTheme {
        Scaffold {
            AnimatedNavHost(
                navController = navController,
                startDestination = Screen.RepositoryListScreen.name) {
                composable(
                    route = Screen.RepositoryListScreen.name,
                    enterTransition = { slideInHorizontally() },
                    exitTransition = { exitTransition }
                ) {
                    RepositoryListScreen { navController.navigate(Screen.RepositoryDetailsScreen.name) }
                }
                composable(
                    route = Screen.RepositoryDetailsScreen.name,
                    popExitTransition = { slideOutHorizontally() },
                    enterTransition = { enterTransition },
                ) {
                    RepositoryDetailsScreen { navController.popBackStack() }
                }
            }
        }
    }
}
