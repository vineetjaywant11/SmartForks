package com.example.smartforks.userpref

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartforks.data.DataStore
import com.example.smartforks.model.Allergies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FoodAllergies(onNext: () -> Unit) {
    val context = LocalContext.current
    val selectedValues = remember { mutableStateListOf<String>() }
    val isSelectedItem: (String) -> Boolean = { it in selectedValues }
    val onChangeState: (String) -> Unit = {
        if (it in selectedValues) selectedValues.remove(it)
        else selectedValues.add(it)
    }
    val store = DataStore(context)
    var additionalText by rememberSaveable { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp), horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Any ingredient allergies?", fontSize = 24.sp)

            Spacer(modifier = Modifier.size(24.dp))

            Text(text = "To offer you the best tailored diet experience we need to know more information about you.")

            Spacer(modifier = Modifier.size(36.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Allergies.entries.forEach { allergy ->
                        AllergyChip(
                            text = allergy.allergy,
                            selected = isSelectedItem(allergy.allergy),
                            onSelected = { onChangeState(allergy.allergy) })
                    }
                }
                Spacer(modifier = Modifier.size(36.dp))
                OutlinedTextField(
                    value = additionalText,
                    onValueChange = { additionalText = it },
                    placeholder = { Text(text = "Anything else you want us to know?") })
            }
        }

        Spacer(modifier = Modifier.size(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        store.saveAllergy(selectedValues.joinToString(",") + " " +additionalText)
                    }
                    Log.d("test", selectedValues.joinToString(",") + " " +additionalText)
                    onNext()
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(text = "Next")
            }
        }

        Spacer(modifier = Modifier.size(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllergyChip(
    text: String,
    selected: Boolean,
    onSelected: () -> Unit
) {
    InputChip(
        modifier = Modifier.padding(6.dp, 6.dp),
        onClick = {
            onSelected()
        },
        label = {
            Text(
                text = text,
                modifier = Modifier.padding(
                    top = 10.dp,
                    start = 12.dp,
                    bottom = 10.dp,
                    end = 12.dp
                ),
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
        },
        selected = selected,
        shape = RoundedCornerShape(20.dp),
        border = InputChipDefaults.inputChipBorder(
            enabled = true,
            selected = selected,
            selectedBorderColor = MaterialTheme.colorScheme.primary,
            selectedBorderWidth = 1.dp
        ),
    )
}