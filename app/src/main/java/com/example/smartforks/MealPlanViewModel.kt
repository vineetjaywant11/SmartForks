package com.example.smartforks

import android.graphics.Bitmap
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
        modelName = "gemini-pro-vision",
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

                var mealPlan = generateMealPlan(response.text.toString())
                _uiState.value = UiState.SuccessObj(mealPlan)
//                response.text?.let { outputContent ->
//                    _uiState.value = UiState.SuccessObj(outputContent)
//                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    fun generateMealPlan(JSON_DATA: String): List<ApiResponse> {
        var apiResponseList = mapJsonResponse(JSON_DATA)
//        val mealPlan = mutableListOf<String>()

//        for (apiResponse in apiResponseList) {
//            // Access properties of each ApiResponse object
//            val day = apiResponse.Day
//            val breakfast = apiResponse.Breakfast
//            val lunch = apiResponse.Lunch
//            val dinner = apiResponse.Dinner
//
//            // Access properties of Breakfast, Lunch, and Dinner objects
//            val breakfastName = breakfast?.name
//            val breakfastIngredients = breakfast?.ingredients
//            val breakfastProcess = breakfast?.process
//            val breakfastMacros = breakfast?.macros
//
//            val lunchName = lunch?.name
//            val lunchIngredients = lunch?.ingredients
//            val lunchProcess = lunch?.process
//            val lunchMacros = lunch?.macros
//
//            val dinnerName = dinner?.name
//            val dinnerIngredients = dinner?.ingredients
//            val dinnerProcess = dinner?.process
//            val dinnerMacros = dinner?.macros
//
//            // Do something with the data
//            // println("Day: $day")
//        }

        return apiResponseList
    }

    fun mapJsonResponse(jsonString: String): List<ApiResponse> {
        val gson = Gson()
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray
        val apiResponseList = mutableListOf<ApiResponse>()

        for (jsonElement in jsonArray) {
            val apiResponse = gson.fromJson(jsonElement, ApiResponse::class.java)
            apiResponseList.add(apiResponse)
        }

        return apiResponseList
    }


}