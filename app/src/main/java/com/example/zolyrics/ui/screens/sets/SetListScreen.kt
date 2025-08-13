package com.example.zolyrics.ui.screens.sets

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.zolyrics.ui.navigation.Screen
import com.example.zolyrics.ui.viewmodel.SongSetViewModel


@Composable
fun SetListScreen(
    viewModel: SongSetViewModel = viewModel(factory = SongSetViewModel.Factory),
    navController: NavController
) {
    val allSets by viewModel.allSets.collectAsStateWithLifecycle(initialValue = emptyList())

    // Local UI state
    var query by remember { mutableStateOf("") }
    var pendingDeleteId by remember { mutableStateOf<String?>(null) }

    val sets = remember(allSets, query) {
        if (query.isBlank()) allSets
        else allSets.filter { it.title.contains(query, ignoreCase = true) }
    }

    if (sets.isEmpty()) {
        EmptyState(
            query = query,
            onQueryChange = { query = it },
            onCreateClick = { navController.navigate("sets/create") }
        )
        return
    }

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            placeholder = { Text("Search sets") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                items = sets,
                key = { it.id } // stable keys
            ) { set ->
                ListItem(
                    headlineContent = {
                        Text(
                            set.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingContent = {
                        Text(
                            "Created ${relativeTime(set.createdAt)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                navController.navigate(Screen.SetDetail.createRoute(set.id))
                            },
                            onLongClick = {
                                pendingDeleteId = set.id
                            }
                        )
                        .padding(vertical = 6.dp)
                )
                HorizontalDivider()
            }
        }

        if (pendingDeleteId != null) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { pendingDeleteId = null },
                title = { Text("Delete set?") },
                text = { Text("This removes the set and its items. Songs themselves are not deleted.") },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            viewModel.deleteSet(pendingDeleteId!!) // implement below
                            pendingDeleteId = null
                        }
                    ) { Text("Delete") }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(
                        onClick = { pendingDeleteId = null }
                    ) { Text("Cancel") }
                }
            )
        }
    }
}

private fun relativeTime(epochMillis: Long): String {
    val now = System.currentTimeMillis()
    val diff = (now - epochMillis).coerceAtLeast(0L)

    val minute = 60_000L
    val hour = 60 * minute
    val day = 24 * hour

    return when {
        diff < hour      -> "${(diff / minute).coerceAtLeast(1)}m ago"
        diff < day       -> "${diff / hour}h ago"
        diff < 14 * day  -> "${diff / day}d ago"
        else             -> java.text.SimpleDateFormat("MMM d", java.util.Locale.getDefault())
            .format(java.util.Date(epochMillis))
    }
}

@Composable
private fun EmptyState(
    query: String,
    onQueryChange: (String) -> Unit,
    onCreateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text("No sets yet", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(4.dp))
        Text(
            "Create your first set to get started.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onCreateClick) { Text("Create set") }
        Spacer(Modifier.height(24.dp))
        // Optional search field stays, so users can tell it’s searchable
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search sets") },
            singleLine = true
        )
    }
}