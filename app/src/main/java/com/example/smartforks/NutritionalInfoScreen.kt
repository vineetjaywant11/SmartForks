package com.example.smartforks

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import java.io.InputStream

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NutritionalInfoScreen(
    modifier: Modifier,
    bakingViewModel: BakingViewModel = viewModel()) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var nutritionalInfo by remember { mutableStateOf("Enter a dish name or take a photo to see nutritional info.") }
    val placeholderPrompt = "Recognise the dish and exact nuritional value of it including the macros"
    val placeholderResult = ""
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by bakingViewModel.uiState.collectAsState()
    val context = LocalContext.current

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

            Button(
                onClick = {
                    val bitmap = imageUri?.let { uriToBitmap(context, it) }
                    if (bitmap != null) {
                        bakingViewModel.sendPrompt(bitmap, prompt)
                    }
                }

            ) {
                Text(
                    text = "Analyse and give details"
                )
            }
        }
        if (uiState is UiState.Loading) {
            CircularProgressIndicator(
//                modifier = Modifier.align(Alignment.CenterHorizontally
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
            Text(
                text = result,
                textAlign = TextAlign.Start,
                color = textColor,
                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            )
        }

    }
}

fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? {
    val contentResolver = context.contentResolver

    val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
    return inputStream?.use {
        BitmapFactory.decodeStream(it)
    }
}
