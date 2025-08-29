package com.battlebucks.battlebucksgithubapp.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(  onSubmit: (String) -> Unit) {

    // palette
    val BgYellow      = Color(0xFFFFF4CC)
    val DarkGreen     = Color(0xFF008000)
    val LightGreenBtn = Color(0xFFA4DB75)
    val TextGreen     = Color(0xFF2B7A0B)
    val BorderGreen   = Color(0xFF4CAF50)
    val GreyText      = Color(0xFF9E9E9E)

    var text by remember { mutableStateOf("") }
    val focus = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    fun submitIfValid() {
        val q = text.trim()
        if (q.isNotEmpty()) {
            onSubmit(q)
            keyboard?.hide()
            focus.clearFocus()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "GitHub User Search",
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
                .background(Brush.verticalGradient(listOf(BgYellow, Color.White)))
                .padding(padding)
        ) {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedCard(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(
                            "Find a profile",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TextGreen, fontSize = 20.sp
                            )
                        )
                        Spacer(Modifier.height(10.dp))

                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            singleLine = true,
                            placeholder = { Text("e.g., shubham-mishra-blip") },
                            leadingIcon = {
                                Icon(Icons.Rounded.Search, contentDescription = "Search", tint = DarkGreen)
                            },
                            trailingIcon = {
                                AnimatedVisibility(visible = text.isNotEmpty()) {
                                    IconButton(onClick = { text = "" }) {
                                        Icon(Icons.Rounded.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { submitIfValid() }),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BorderGreen,
                                unfocusedBorderColor = BorderGreen.copy(alpha = 0.4f),
                                cursorColor = DarkGreen
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        val suggestions = listOf("shubham-mishra-blip", "JakeWharton", "octocat", "google")
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            suggestions.forEach { who ->
                                AssistChip(
                                    onClick = {
                                        text = who
                                        submitIfValid()
                                    },
                                    label = { Text(who) },
                                    border = AssistChipDefaults.assistChipBorder(
                                        borderColor = BorderGreen, borderWidth = 1.dp
                                    ),
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = { submitIfValid() },
                            enabled = text.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightGreenBtn,
                                contentColor = TextGreen,
                                disabledContainerColor = LightGreenBtn.copy(alpha = 0.4f),
                                disabledContentColor = TextGreen.copy(alpha = 0.6f)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Search", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    "Tip: Use exact username for best results.",
                    style = MaterialTheme.typography.bodySmall,
                    color = GreyText
                )
            }
        }
    }
}
