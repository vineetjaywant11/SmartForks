package com.example.smartforks

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.smartforks.data.DataStore
import com.example.smartforks.model.UserPreferences
import com.example.smartforks.ui.theme.SmartForksTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartForksTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                val store = DataStore(this)
                var screen by remember { mutableStateOf("User") }
                runBlocking {
                    if (store.getHeight.first().isNotEmpty()) {
                        screen = "Meal"
                    }
                }

                val userPrefs = runBlocking {
                    UserPreferences(
                        height = store.getHeight.first(),
                        weight = store.getWeight.first(),
                        dietaryPref = store.getDiet.first(),
                        allergies = store.getAllergy.first(),
                        goal = store.getGoal.first()
                    )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(title = {

                            },
                                modifier = Modifier.padding(horizontal = 16.dp),
                                actions = {
                                    Icons.Filled.Settings
                                })
                        },
                        bottomBar = { BottomNavigation(navController = navController) }
                    ) {
                        NavigationGraph(
                            navController = navController,
                            modifier = Modifier.padding(it),
                            prompt = createMealPlanPrompt(userPrefs = userPrefs)
                        )
                        /*val store = DataStore(this)
                        var screen by remember { mutableStateOf("User") }
                        runBlocking {
                            if (store.getHeight.first().isNotEmpty()) {
                                screen = "Meal"
                            }
                        }

                        var prompt by remember {
                            mutableStateOf("")
                        }

                        val userPrefs = runBlocking {
                            UserPreferences(
                                height = store.getHeight.first(),
                                weight = store.getWeight.first(),
                                dietaryPref = store.getDiet.first(),
                                allergies = store.getAllergy.first(),
                                goal = store.getGoal.first()
                            )
                        }
                        when (screen) {
                            "User" -> UserPrefScreen(getPrompt = {
                                Log.d("test1", it)
                                prompt = it
                                screen = "Meal"
                            })

                            "Meal" -> MealPlannerScreen(prompt = createMealPlanPrompt(userPrefs))
                        }*/
                    }

                }
            }
        }
    }
}

fun createMealPlanPrompt(userPrefs: UserPreferences): String {
    return """
        make a daily step-by-step meal plan(breakfast, lunch, dinner) for me with a detailed process
        height: ${userPrefs.height} cm
        weight: ${userPrefs.weight}lb
        dietary pref: ${userPrefs.dietaryPref ?: "None"}
        allergies: ${userPrefs.allergies ?: "None"}
        goal: ${userPrefs.goal}
        output format(Markdown): name, macros, ingredients, process
        
        don't add anything like time or macros or : in front of headings like breakfast, lunch and dinner
        
    """
}