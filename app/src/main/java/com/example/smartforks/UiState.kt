package com.example.smartforks

import com.example.smartforks.model.ApiResponse

/**
 * A sealed hierarchy describing the state of the text generation.
 */
sealed interface UiState {

    /**
     * Empty state when the screen is first shown
     */
    object Initial : UiState

    /**
     * Still loading
     */
    object Loading : UiState

    /**
     * Text has been generated
     */
    data class Success(val outputText: String) : UiState
    data class SuccessObj(val outputObj: List<ApiResponse>) : UiState

    /**
     * There was an error generating text
     */
    data class Error(val errorMessage: String) : UiState
}