package com.example.smartforks

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartforks.ui.theme.SmartForksTheme
import android.net.Uri

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionalInfoScreen() {
    var searchText by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var nutritionalInfo by remember { mutableStateOf("Enter a dish name or take a photo to see nutritional info.") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            // Process the image here. For now, just set a placeholder response.
            nutritionalInfo = "Nutritional info for the captured dish: [Placeholder Data]"
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nutritional Information") },
            )
        },
        content = { innerPadding ->  // This PaddingValues tells you the area not occluded by app bars etc.
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)  // Apply the padding to the content inside the Scaffold
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        label = { Text("Search for a dish") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { /* Search and fetch nutritional data */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Get Nutritional Info")
                    }
                    Button(
                        onClick = { launcher.launch(null) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Take a Photo")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(nutritionalInfo, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionalInfoScreen() {
    SmartForksTheme {
        NutritionalInfoScreen()
    }
}
