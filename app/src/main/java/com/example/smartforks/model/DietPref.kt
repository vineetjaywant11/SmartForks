package com.example.smartforks.model

enum class DietPref(val dietPref: String) {

    VEGAN("Vegan"),
    PALEO("Paleo"),
    DUKAN("Dukan"),
    VEGETARIAN("Vegetarian"),
    ATKINS("Atkins"),
    INTERMITTENT_FASTING("Intermittent Fasting"),
    NONE("None");

    companion object {
        private val map = entries.associateBy { it.dietPref }
        infix fun from(value: String) = map[value]
    }
}