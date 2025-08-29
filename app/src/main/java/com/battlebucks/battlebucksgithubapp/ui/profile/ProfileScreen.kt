package com.battlebucks.battlebucksgithubapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CallSplit
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.battlebucks.battlebucksgithubapp.data.network.model.response.GithubRepoDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(username: String, vm: ProfileViewModel) {
    val BgYellow      = Color(0xFFFFF4CC)
    val DarkGreen     = Color(0xFF008000)
    val LightGreenBtn = Color(0xFFA4DB75)
    val TextGreen     = Color(0xFF2B7A0B)
    val BorderGreen   = Color(0xFF4CAF50)
    val GreyText      = Color(0xFF9E9E9E)

    val uiState by vm.uiState.collectAsState()
    val paging = vm.reposPaging.collectAsLazyPagingItems()

    LaunchedEffect(username) { vm.load(username) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        username,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = TextGreen
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(BgYellow, Color.White))
                )
                .padding(padding)
        ) {
            when (uiState) {
                is ProfileUiState.Loading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is ProfileUiState.Error -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        (uiState as ProfileUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is ProfileUiState.Success -> {
                    val user = (uiState as ProfileUiState.Success).user

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            ElevatedCard(
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    Modifier
                                        .padding(20.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = user.avatarUrl,
                                        contentDescription = "Avatar",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(72.dp)
                                            .clip(CircleShape)
                                            .shadow(4.dp, CircleShape)
                                    )
                                    Spacer(Modifier.width(16.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(
                                            text = user.login,
                                            style = MaterialTheme.typography.titleLarge.copy(color = TextGreen)
                                        )
                                        if (!user.bio.isNullOrBlank()) {
                                            Spacer(Modifier.height(6.dp))
                                            Text(
                                                text = user.bio!!,
                                                style = MaterialTheme.typography.bodyMedium,
                                                maxLines = 3,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(Modifier.height(10.dp))
                                        Row(
                                            Modifier
                                                .horizontalScroll(rememberScrollState()),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            AssistChip(
                                                onClick = {},
                                                label = { Text("Followers: ${user.followersCount}") },
                                                leadingIcon = {
                                                    Icon(Icons.Rounded.People, contentDescription = null, tint = DarkGreen)
                                                },
                                                border = AssistChipDefaults.assistChipBorder(
                                                    borderColor = BorderGreen, borderWidth = 1.dp
                                                ),
                                                colors = AssistChipDefaults.assistChipColors(containerColor = Color.White)
                                            )
                                            AssistChip(
                                                onClick = {},
                                                label = { Text("Public repos: ${user.publicRepos}") },
                                                leadingIcon = {
                                                    Icon(Icons.Rounded.Code, contentDescription = null, tint = DarkGreen)
                                                },
                                                border = AssistChipDefaults.assistChipBorder(
                                                    borderColor = BorderGreen, borderWidth = 1.dp
                                                ),
                                                colors = AssistChipDefaults.assistChipColors(containerColor = Color.White)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                "Repositories",
                                style = MaterialTheme.typography.titleMedium.copy(color = TextGreen)
                            )
                        }

                        items(paging.itemCount) { index ->
                            paging[index]?.let { RepoCard(it) }
                        }

                        paging.apply {
                            when {
                                loadState.refresh is androidx.paging.LoadState.Loading ||
                                        loadState.append is androidx.paging.LoadState.Loading -> {
                                    item {
                                        Box(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) { CircularProgressIndicator() }
                                    }
                                }

                                loadState.refresh is androidx.paging.LoadState.Error -> {
                                    val e = loadState.refresh as androidx.paging.LoadState.Error
                                    item {
                                        Text(
                                            "Failed to load: ${e.error.message}",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }

                                loadState.append is androidx.paging.LoadState.Error -> {
                                    val e = loadState.append as androidx.paging.LoadState.Error
                                    item {
                                        Text(
                                            "More items failed: ${e.error.message}",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                ProfileUiState.Idle -> { /* no-op */ }
            }
        }
    }
}

@Composable
private fun RepoCard(repo: GithubRepoDto) {
    val DarkGreen   = Color(0xFF008000)
    val TextGreen   = Color(0xFF2B7A0B)
    val BorderGreen = Color(0xFF4CAF50)
    val GreyText    = Color(0xFF9E9E9E)

    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                repo.name,
                style = MaterialTheme.typography.titleMedium.copy(color = TextGreen),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!repo.description.isNullOrBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    repo.description!!,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {},
                    label = { Text("${repo.stars}") },
                    leadingIcon = {
                        Icon(Icons.Rounded.Star, contentDescription = null, tint = DarkGreen)
                    },
                    border = AssistChipDefaults.assistChipBorder(
                        borderColor = BorderGreen, borderWidth = 1.dp
                    ),
                    colors = AssistChipDefaults.assistChipColors(containerColor = Color.White)
                )
                AssistChip(
                    onClick = {},
                    label = { Text("${repo.forks}") },
                    leadingIcon = {
                        Icon(Icons.Rounded.CallSplit, contentDescription = null, tint = DarkGreen)
                    },
                    border = AssistChipDefaults.assistChipBorder(
                        borderColor = BorderGreen, borderWidth = 1.dp
                    ),
                    colors = AssistChipDefaults.assistChipColors(containerColor = Color.White)
                )
                repo.language?.let { lang ->
                    AssistChip(
                        onClick = {},
                        label = { Text(lang) },
                        leadingIcon = {
                            Icon(Icons.Rounded.Code, contentDescription = null, tint = DarkGreen)
                        },
                        border = AssistChipDefaults.assistChipBorder(
                            borderColor = BorderGreen, borderWidth = 1.dp
                        ),
                        colors = AssistChipDefaults.assistChipColors(containerColor = Color.White)
                    )
                }
            }

            Spacer(Modifier.height(4.dp))
            Text("Updated info may vary", style = MaterialTheme.typography.bodySmall, color = GreyText)
        }
    }
}
