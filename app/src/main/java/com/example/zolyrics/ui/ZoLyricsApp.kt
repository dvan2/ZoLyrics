package com.example.zolyrics.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zolyrics.ui.navigation.Screen
import com.example.zolyrics.ui.screens.favorites.FavoritesScreen
import com.example.zolyrics.ui.screens.home.HomeScreen

@Composable
fun ZoLyricsApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold (
        bottomBar = {
            BottomNavigationBar(
                selectedTab = currentRoute ?: Screen.Home.route,
                onTabSelected = { navController.navigate(it) }
            )
        }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route){
                HomeScreen()
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar (

    ){
        NavigationBarItem(
            selected = selectedTab == Screen.Home.route,
            onClick = { onTabSelected(Screen.Home.route) },
            icon = {
                Icon(Icons.Default.Home, contentDescription = "Home",)
            },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = selectedTab == Screen.Favorites.route,
            onClick = { onTabSelected(Screen.Favorites.route)},
            icon = {
                Icon(Icons.Default.Favorite, contentDescription = "Favorites")
            },
            label = { Text("Favorites")}
        )
    }
}