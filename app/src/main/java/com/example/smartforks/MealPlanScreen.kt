package com.example.smartforks

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartforks.model.ApiResponse
import com.google.gson.Gson
import com.google.gson.JsonParser
import dev.jeziellago.compose.markdowntext.MarkdownText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlannerScreen(
    mealPlanViewModel: MealPlanViewModel = viewModel(),
    prompt: String
) {
    var mealPlan by remember { mutableStateOf<List<String>>(listOf()) }
    val uiState by mealPlanViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val placeholderResult = stringResource(R.string.results_placeholder)
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    LaunchedEffect(Unit) {
        Log.d("testMP", prompt)
        mealPlanViewModel.sendPrompt(prompt)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Box {
                        Text("Meal Planner")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Open Settings or Help */ }) {
                        Icon(Icons.Outlined.Info, contentDescription = "Info")
                    }
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState is UiState.Loading) {
                    Log.d("Loading", "Here")
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.Black
                    )
                } else {
                    var textColor = MaterialTheme.colorScheme.onSurface
                    if (uiState is UiState.Error) {
                        textColor = MaterialTheme.colorScheme.error
                        result = (uiState as UiState.Error).errorMessage
                    } else if (uiState is UiState.Success) {
                        textColor = MaterialTheme.colorScheme.onSurface
                        result = (uiState as UiState.Success).outputText
                    }
                    val scrollState = rememberScrollState()

                    MarkdownText(
                        markdown = splitMeals(result)[0],
                        color = textColor,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    )
                }
            }
        }
    }
}

fun splitMeals(input: String): List<String> {
    val keywords = listOf("**Breakfast", "**Lunch", "**Dinner")
    val parts = mutableListOf<String>()
    var startIndex = 0

    // Loop over the string to find the keywords
    for (i in input.indices) { // Check boundary to avoid out of bounds
        if (i + 10 < input.length) {
            val substring = input.substring(i, i + 7)
            if (keywords.any { substring.startsWith(it) }) {
                if (startIndex != i) {
                    parts.add(input.substring(startIndex, i))
                }
                startIndex = i
            }
        }
    }
    // Add the last part
    parts.add(input.substring(startIndex))

    return parts
}
