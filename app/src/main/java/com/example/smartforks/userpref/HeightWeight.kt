package com.example.smartforks.userpref

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartforks.data.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HeightWeight(onNext: () -> Unit) {
    val context = LocalContext.current
    var height by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    val store = DataStore(context)
    var goal by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(24.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Let's get started!", fontSize = 24.sp)
        Spacer(modifier = Modifier.size(24.dp))
        Text(text = "Please enter your height and weight.")
        Spacer(modifier = Modifier.padding(36.dp))

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Number)
        )

        // Spacing between text fields
        Spacer(modifier = Modifier.padding(12.dp))

        // Weight input field
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (lb)") },
            keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Number)
        )

        // Spacing between text fields
        Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = goal,
            onValueChange = { goal = it },
            placeholder = { Text(text = "What is your goal?") })
    }

    // Submit button
    Button(
        onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                store.saveHeight(height)
                store.saveWeight(weight)
                store.saveGoal(goal)
            }
            onNext()
        },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Next")
    }
}