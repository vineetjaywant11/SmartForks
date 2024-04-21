package com.example.smartforks

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartforks.model.ApiResponse
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MealPlanViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.apiKey
    )

    fun sendPrompt(
        prompt: String
    ) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )

                response.text?.let { outputContent ->
                    Log.d("Res", outputContent)
                    _uiState.value = UiState.Success(outputContent)
                }
//                val mealPlan = mapJsonResponse(response.text.toString())
//                _uiState.value = UiState.SuccessObj(mealPlan)
//                response.text?.let { outputContent ->
//                    for(c in outputContent)
////                    Log.d("testing here: ", outputContent)
//                    _uiState.value = UiState.SuccessObj(mapJsonResponse(outputContent))
//                }
//                val mealPlan = mapJsonResponse(response.text.toString())
//                _uiState.value = UiState.SuccessObj(mealPlan)
                response.text?.let { outputContent ->
                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.toString())
            }
        }
    }

    private fun mapJsonResponse(jsonString: String): List<ApiResponse> {
        val gson = Gson()
        /*val startIndex = jsonString.indexOf('n') + 1
        val endIndex = jsonString.lastIndexOf('`') - 1
        val jsonPart = jsonString.substring(startIndex, endIndex)*/
        val jsonArray = JsonParser.parseString(jsonString.trimIndent()).asJsonArray
        val apiResponseList = mutableListOf<ApiResponse>()

        for (jsonElement in jsonArray) {
            val apiResponse = gson.fromJson(jsonElement, ApiResponse::class.java)
            apiResponseList.add(apiResponse)
        }

        return apiResponseList
    }


}
