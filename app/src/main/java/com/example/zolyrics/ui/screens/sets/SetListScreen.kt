package com.example.zolyrics.ui.screens.sets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.zolyrics.ui.navigation.Screen
import com.example.zolyrics.ui.viewmodel.SongSetViewModel
import java.util.Date

@Composable
fun SetListScreen(
    viewModel: SongSetViewModel = viewModel(factory = SongSetViewModel.Factory),
    navController: NavController
) {
    val sets by viewModel.allSets.collectAsState()

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(sets) { set ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate(Screen.SetDetail.createRoute(set.id))
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(set.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Created: ${Date(set.createdAt)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

    }

}