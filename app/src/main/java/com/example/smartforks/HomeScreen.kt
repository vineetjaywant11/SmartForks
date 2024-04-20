package com.example.smartforks
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartforks.ui.theme.SmartForksTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Healthy Eating App") },
                actions = {
                    IconButton(onClick = { /* Settings Screen Navigation */ }) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Welcome to Your Health Journey!",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { /* Navigate to Meal Planner */ },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text("Meal Planner")
                }
                Button(
                    onClick = { /* Navigate to Shopping List */ },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text("Shopping List")
                }
                Button(
                    onClick = { /* Navigate to Nutritional Information */ },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text("Nutritional Information")
                }
            }
        }
    }

//    @Preview(showBackground = true)
    @Composable
    fun PreviewHomeScreen() {
        SmartForksTheme {
            HomeScreen()
        }
    }
}