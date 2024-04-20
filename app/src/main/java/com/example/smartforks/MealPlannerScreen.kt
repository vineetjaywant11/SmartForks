package com.example.smartforks

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartforks.ui.theme.SmartForksTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MealPlannerScreen() {
    var vegetarian by remember { mutableStateOf(false) }
    var glutenFree by remember { mutableStateOf(false) }
    var vegan by remember { mutableStateOf(false) }
    var mealPlan by remember { mutableStateOf<List<String>>(listOf()) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Meal Planner") },
                actions = {
                    IconButton(onClick = { /* Open Settings or Help */ }) {
                        Icon(Icons.Outlined.Info, contentDescription = "Info")
                    }
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Set your dietary preferences:", style = MaterialTheme.typography.titleLarge)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    CheckboxWithLabel("Vegetarian", vegetarian) { vegetarian = it }
                    CheckboxWithLabel("Gluten-free", glutenFree) { glutenFree = it }
                    CheckboxWithLabel("Vegan", vegan) { vegan = it }
                }
                Button(
                    onClick = { mealPlan = generateMealPlan(vegetarian, glutenFree, vegan) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Generate Meal Plan")
                }
                if (mealPlan.isNotEmpty()) {
                    Text("Your Weekly Meal Plan:", style = MaterialTheme.typography.titleMedium)
                    mealPlan.forEach { meal ->
                        Text(meal, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun CheckboxWithLabel(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}

fun generateMealPlan(vegetarian: Boolean, glutenFree: Boolean, vegan: Boolean): List<String> {
    // Placeholder logic to generate a meal plan
    return listOf("Breakfast: Oatmeal", "Lunch: Salad", "Dinner: Stir Fry")
}

@Preview(showBackground = true)
@Composable
fun PreviewMealPlannerScreen() {
    SmartForksTheme {
        MealPlannerScreen()
    }
}
