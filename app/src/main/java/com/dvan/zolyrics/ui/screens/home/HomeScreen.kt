package com.dvan.zolyrics.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dvan.zolyrics.data.model.Song
import com.dvan.zolyrics.ui.navigation.Screen
import com.dvan.zolyrics.ui.screens.components.AddToSetBottomSheet
import com.dvan.zolyrics.ui.screens.components.LocalSnackbarHostState
import com.dvan.zolyrics.ui.screens.components.SongCard
import com.dvan.zolyrics.ui.viewmodel.SongSetViewModel
import com.dvan.zolyrics.ui.viewmodel.SongViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val viewModel: SongViewModel = hiltViewModel()
    val songs by viewModel.localSongs.collectAsState(initial = emptyList())

    val songSetViewModel:  SongSetViewModel = hiltViewModel()
    val sets by songSetViewModel.allSets.collectAsState()

    var selectedSongId by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = LocalSnackbarHostState.current

    var query by rememberSaveable { mutableStateOf("") }
    val filtered = remember(songs, query) {
        val q = query.trim()
        if (q.isEmpty()) songs
        else songs.filter { s ->
            s.title.contains(q, ignoreCase = true) ||
                    s.artistName.contains(q, ignoreCase = true)
        }
    }

    Column(Modifier.fillMaxSize()) {
        OutlinedSearchField(
            value = query,
            onChange = { query = it },
            onClear = { query = "" },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        SongListScreen(
            songs = filtered,
            onSongClick = { id ->
                viewModel.loadLyrics(id)
                navController.navigate(Screen.SongDetail.createRoute(id))
            },
            onSongLongClick = { id -> selectedSongId = id },
            modifier = Modifier.fillMaxSize()
        )
    }
    AddToSetBottomSheet(
        selectedSongId = selectedSongId,
        sets = sets,
        songSetViewModel = songSetViewModel,
        sheetState = sheetState,
        snackbarHostState = snackbarHostState,
        onDismiss = { selectedSongId = null }
    )
}


@Composable
private fun OutlinedSearchField(
    value: String,
    onChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        singleLine = true,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        placeholder = { Text("Search songs") },
        modifier = modifier
    )
}

@Composable
fun SongListScreen(
    songs: List<Song>,
    onSongClick: (String) -> Unit,
    onSongLongClick: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    LazyColumn ( modifier = modifier, contentPadding = PaddingValues(bottom = 24.dp)) {
        items(songs, key = { it.id }) { song ->
            SongCard(
                song = song,
                onClick = { onSongClick(song.id) },
                onLongClick = { onSongLongClick?.let { it(song.id) } }
            )
            HorizontalDivider()
        }
    }
}


