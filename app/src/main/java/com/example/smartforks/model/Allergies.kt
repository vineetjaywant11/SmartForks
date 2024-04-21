package com.example.smartforks.model

enum class Allergies(val allergy: String) {
    NONE("None"),
    GLUTEN("Gluten"),
    DAIRY("Diary"),
    EGG("Egg"),
    SOY("Soy"),
    PEANUT("Peanut"),
    WHEAT("Wheat"),
    MILK("Milk"),
    FISH("Fish");

    companion object {
        private val map = entries.associateBy { it.allergy }
        infix fun from(value: String) = map[value]
    }
}