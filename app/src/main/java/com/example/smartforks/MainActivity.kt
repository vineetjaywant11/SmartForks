package com.example.smartforks

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.smartforks.data.DataStore
import com.example.smartforks.model.UserPreferences
import com.example.smartforks.ui.theme.SmartForksTheme
import com.example.smartforks.userpref.UserPrefScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartForksTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val store = DataStore(this)
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