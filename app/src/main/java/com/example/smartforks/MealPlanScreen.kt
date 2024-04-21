package com.example.smartforks
import java.util.regex.Pattern

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
//                    Log.d("here:", result)
                    val (breakfast, lunch, dinner) = splitMealContent(result.trimIndent())
                    MarkdownText(
                        markdown = "## LUNCH\n" + lunch,
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
    val pattern = Pattern.compile("""(?<=\*\*Breakfast\*\*)(.*?)(?=\*\*Lunch\*\*)""", Pattern.DOTALL)
    val breakfast = pattern.matcher(content).let {
        if (it.find()) it.group(1).trim() else ""
    }

    val pattern2 = Pattern.compile("""(?<=\*\*Lunch\*\*)(.*?)(?=\*\*Dinner\*\*)""", Pattern.DOTALL)
    val lunch = pattern2.matcher(content).let {
        if (it.find()) it.group(1).trim() else ""
    }

    val pattern3 = Pattern.compile("""(?<=\*\*Dinner\*\*)(.*?)$""", Pattern.DOTALL)
    val dinner = pattern3.matcher(content).let {
        if (it.find()) it.group(1).trim() else ""
    }

    return Triple(breakfast, lunch, dinner)
}

//fun splitMealContent(content: String): Triple<String, String, String> {
//    // Pattern to capture the content after "**Breakfast" up to "**Lunch"
//    val pattern = Pattern.compile("""\*\*.*?Breakfast.*?(.*?)(?=\*\*.*?Lunch)""", Pattern.DOTALL)
//    val breakfast = pattern.matcher(content).let {
//        if (it.find()) it.group(1).trim() else ""
//    }
//
//    // Pattern to capture the content after "**Lunch" up to "**Dinner"
//    val pattern2 = Pattern.compile("""\*\*.*?Lunch.*?(.*?)(?=\*\*.*?Dinner)""", Pattern.DOTALL)
//    val lunch = pattern2.matcher(content).let {
//        if (it.find()) it.group(1).trim() else ""
//    }
//
//    // Pattern to capture the content after "**Dinner" to the end of the string
//    val pattern3 = Pattern.compile("""\*\*.*?Dinner.*?(.*?)$""", Pattern.DOTALL)
//    val dinner = pattern3.matcher(content).let {
//        if (it.find()) it.group(1).trim() else ""
//    }
//
//    return Triple(breakfast, lunch, dinner)
//}
