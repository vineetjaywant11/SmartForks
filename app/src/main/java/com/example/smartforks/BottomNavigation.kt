package com.example.smartforks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState


sealed class BottomNavItem(var title: String, var icon: ImageVector, var screenRoute: String) {
    data object Meals : BottomNavItem("Meals", Icons.Filled.List, "meals")
    data object Info : BottomNavItem("Info", Icons.Filled.Info, "info")
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier,
    prompt: String,
) {
    NavHost(navController, startDestination = BottomNavItem.Meals.screenRoute) {
        composable(BottomNavItem.Meals.screenRoute) {
            MealPlannerScreen(modifier, prompt =  prompt)
        }
        composable(BottomNavItem.Info.screenRoute) {
            NutritionalInfoScreen(modifier)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Meals,
        BottomNavItem.Info,
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = "") },
                selected = currentRoute == item.screenRoute,
                label = {
                    Text(text = item.title)
                },
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}