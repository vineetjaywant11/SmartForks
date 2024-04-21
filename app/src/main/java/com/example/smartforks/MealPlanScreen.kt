package com.example.smartforks

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlannerScreen(
    modifier: Modifier,
    mealPlanViewModel: MealPlanViewModel = viewModel(),
    prompt: String
) {
    val uiState by mealPlanViewModel.uiState.collectAsState()
    val placeholderResult = stringResource(R.string.results_placeholder)
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    LaunchedEffect(Unit) {
        Log.d("testMP", prompt)
        mealPlanViewModel.sendPrompt(prompt)
    }


    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Meal plan for today",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            if (uiState is UiState.Loading) {
                Log.d("Loading", "Here")
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
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
                val (breakfast, lunch, dinner) = splitMealContent(result.trimIndent())
                var state by remember { mutableIntStateOf(0) }
                val titles = listOf("Breakfast", "Lunch", "Dinner")
                PrimaryTabRow(
                    selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selected = state == index,
                            onClick = { state = index },
                            text = {
                                Text(
                                    text = title,
                                    color = if (state == index) MaterialTheme.colorScheme.primary else Color(
                                        0xFF747474
                                    )
                                )
                            }
                        )
                    }
                }
                if (state == 0) {
                    MarkdownText(
                        markdown = breakfast,
                        color = textColor,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    )
                } else if (state == 1) {
                    MarkdownText(
                        markdown = lunch,
                        color = textColor,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    )
                } else {
                    MarkdownText(
                        markdown = dinner,
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

fun splitMealContent(content: String): Triple<String, String, String> {
    val pattern = Pattern.compile("""(?<=Breakfast)(.*?)(?=Lunch)""", Pattern.DOTALL)
    val breakfast = pattern.matcher(content).let {
        if (it.find()) it.group(1).trim() else ""
    }

    val pattern2 = Pattern.compile("""(?<=Lunch)(.*?)(?=Dinner)""", Pattern.DOTALL)
    val lunch = pattern2.matcher(content).let {
        if (it.find()) it.group(1).trim() else ""
    }

    val pattern3 = Pattern.compile("""(?<=Dinner)(.*?)$""", Pattern.DOTALL)
    val dinner = pattern3.matcher(content).let {
        if (it.find()) it.group(1).trim() else ""
    }

    return Triple(breakfast, lunch, dinner)
}
