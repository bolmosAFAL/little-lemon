package com.siade.little_lemon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.siade.little_lemon.composables.Home as HomeScreen
import com.siade.little_lemon.composables.Onboarding as OnboardingScreen
import com.siade.little_lemon.composables.Profile as ProfileScreen

@Composable
fun MyNavigation(
    navController: NavHostController,
    menuItems: List<MenuItemRoom>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sharedPreferences =
        context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)

    val isRegistered = !sharedPreferences.getString("first_name", "").isNullOrBlank()
    val startDestination = if (isRegistered) Home.route else Onboarding.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Onboarding.route) {
            OnboardingScreen(navController = navController)
        }
        composable(Home.route) {
            HomeScreen(navController = navController, menuItems = menuItems)
        }
        composable(Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}
