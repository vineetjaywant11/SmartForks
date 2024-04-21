package com.example.smartforks.userpref

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartforks.data.DataStore
import com.example.smartforks.model.Allergies
import com.example.smartforks.model.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun UserPrefScreen(modifier: Modifier = Modifier, getPrompt: (String) -> Unit) {
    val currentScreen = remember { mutableStateOf(Screen.HeightWeight) }
    val store = DataStore(LocalContext.current)
    val userPrefs = runBlocking {
        UserPreferences(
            height = store.getHeight.first(),
            weight = store.getWeight.first(),
            dietaryPref = store.getDiet.first(),
            allergies = store.getAllergy.first(),
            goal = store.getGoal.first()
        )
    }

    Column(modifier = modifier) {
        when (currentScreen.value) {
            Screen.HeightWeight -> HeightWeight(onNext = {
                currentScreen.value = Screen.FoodAllergies
            })

            Screen.FoodAllergies -> FoodAllergies(onNext = {
                currentScreen.value = Screen.DietPreferences
            })

            Screen.DietPreferences -> DietPreferences(onNext = {
                Log.d("test", userPrefs.toString())
                getPrompt(
                    createMealPlanPrompt(
                        userPrefs = userPrefs
                    )
                )
            })
        }
    }
}

enum class Screen {
    HeightWeight,
    FoodAllergies,
    DietPreferences
}

fun createMealPlanPrompt(userPrefs: UserPreferences): String {
    return """
        make a step-by-step meal plan(breakfast, lunch, dinner) for me with a detailed process
        height: ${userPrefs.height} cm
        weight: ${userPrefs.weight}lb
        dietary pref: ${userPrefs.dietaryPref ?: "None"}
        allergies: ${userPrefs.allergies ?: "None"}
        goal: ${userPrefs.goal}
        output format(Markdown): name, macros, ingredients, process
    """
}