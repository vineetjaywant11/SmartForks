package com.example.smartforks

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartforks.ui.theme.SmartForksTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen() {
    var shoppingItems = remember { mutableStateListOf("Tomatoes", "Chicken Breast", "Spinach", "Almonds") }
    val newItem = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Shopping List") },
                actions = {
                    IconButton(onClick = { /* Settings or Info */ }) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (newItem.value.isNotBlank()) {
                    shoppingItems.add(newItem.value)
                    newItem.value = ""
                }
            }) {
                Icon(Icons.Outlined.Add, contentDescription = "Add Item")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)  // Use padding from Scaffold to adjust content layout
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = newItem.value,
                        onValueChange = { newItem.value = it },
                        label = { Text("Add new item") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(shoppingItems) { item ->
                            ShoppingListItem(item, onCheckedChange = { checked ->
                                if (checked) {
                                    shoppingItems.remove(item)
                                } else {
                                    shoppingItems.add(item)
                                }
                            })
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ShoppingListItem(item: String, onCheckedChange: (Boolean) -> Unit) {
    var isChecked by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { checked ->
                isChecked = checked
                onCheckedChange(checked)
            }
        )
        Text(item, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShoppingListScreen() {
    SmartForksTheme {
        ShoppingListScreen()
    }
}