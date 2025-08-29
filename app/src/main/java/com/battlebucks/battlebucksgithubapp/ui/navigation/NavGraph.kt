package com.battlebucks.battlebucksgithubapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.battlebucks.battlebucksgithubapp.ui.profile.ProfileScreen
import com.battlebucks.battlebucksgithubapp.ui.profile.ProfileViewModel
import com.battlebucks.battlebucksgithubapp.ui.search.SearchScreen

object Routes {
    const val SEARCH = "search"
    const val PROFILE = "profile/{username}"
}

@Composable
fun AppNavHost(nav: NavHostController) {
    NavHost(navController = nav, startDestination = Routes.SEARCH) {
        composable(Routes.SEARCH) {
            SearchScreen(
                onSubmit = { username ->
                    nav.navigate("profile/${username.trim()}")
                }
            )
        }
        composable(Routes.PROFILE) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()
            val vm: ProfileViewModel = hiltViewModel()
            ProfileScreen(username = username, vm = vm)
        }
    }
}