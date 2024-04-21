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
import com.example.smartforks.ui.theme.SmartForksTheme
import com.example.smartforks.userpref.UserPrefScreen
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
                    val currentScreen = remember { mutableStateOf("User") }
                    var prompt by remember {
                        mutableStateOf("")
                    }
                    when (currentScreen.value) {
                        "User" -> UserPrefScreen(getPrompt = {
                            Log.d("test1", it)
                            prompt = it
                            currentScreen.value = "Meal"
                        })

                        "Meal" -> MealPlannerScreen(prompt = prompt)
                    }
                }
            }
        }
    }
}