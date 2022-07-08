package com.lorenzovalentijn.github.repository.browser

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
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
                    RepositoryListScreen { navController.navigate("${Screen.RepositoryDetailsScreen.name}/abnamrocoesd/airflow") }
                }
                composable(
                    route = "${Screen.RepositoryDetailsScreen.name}/{user}/{repo}",
                    arguments = listOf(
                        navArgument("user") { type = NavType.StringType },
                        navArgument("repo") { type = NavType.StringType }
                    ),
                    enterTransition = { enterTransition },
                    exitTransition = { slideOutHorizontally() },
                ) {
                    RepositoryDetailsScreen(
                        it.arguments?.getString("user"),
                        it.arguments?.getString("repo")
                    ) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}
