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
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NutritionalInfoScreen(modifier: Modifier) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var nutritionalInfo by remember { mutableStateOf("Enter a dish name or take a photo to see nutritional info.") }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
            }
        }
    )

    // This PaddingValues tells you the area not occluded by app bars etc.
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                )
            }

            Button(
                onClick = {
                    galleryLauncher.launch("image/*")
                }
            ) {
                Text(
                    text = "Pick image"
                )
            }
        }

    }
}
