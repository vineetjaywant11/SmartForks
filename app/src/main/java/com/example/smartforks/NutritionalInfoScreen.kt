package com.example.smartforks

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.io.InputStream

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NutritionalInfoScreen(
    modifier: Modifier,
    bakingViewModel: BakingViewModel = viewModel()
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var nutritionalInfo by remember { mutableStateOf("Enter a dish name or take a photo to see nutritional info.") }

    val placeholderPrompt = """
        Recognise the dish and exact nutritional value of it including the macros
        Output format: Markdown
        
        Description: Short description of the dish in 2-3 sentences
        
        Macro: Value
    """.trimIndent()
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    val uiState by bakingViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val placeholderResult = ""
    var result by rememberSaveable { mutableStateOf(placeholderResult) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri = it } }
    )

    Surface(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Pick image")
            }

            imageUri?.let {
                Button(
                    onClick = {
                        val bitmap = uriToBitmap(context, it)
                        bitmap?.let { bmp ->
                            bakingViewModel.sendPrompt(bmp, prompt)
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Analyse and give details")
                }
            }

            if (uiState is UiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
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

                MarkdownText(
                    markdown = result,
                    color = textColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                )
            }
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
