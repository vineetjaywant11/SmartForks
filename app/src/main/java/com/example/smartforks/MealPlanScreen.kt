package com.example.smartforks

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartforks.model.ApiResponse
import com.google.gson.Gson
import com.google.gson.JsonParser

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MealPlannerScreen(
    mealPlanViewModel: MealPlanViewModel = viewModel(),
    prompt: String
) {
    var mealPlan by remember { mutableStateOf<List<String>>(listOf()) }
    val uiState by mealPlanViewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        mealPlanViewModel.sendPrompt(prompt)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Box {
                        Text("Meal Planner")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Open Settings or Help */ }) {
                        Icon(Icons.Outlined.Info, contentDescription = "Info")
                    }
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Set your dietary preferences:", style = MaterialTheme.typography.titleLarge)

                Button(
                    onClick = { mealPlan = generateMealPlan() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Generate Meal Plan")
                }
                if (mealPlan.isNotEmpty()) {
                    Text("Your Weekly Meal Plan:", style = MaterialTheme.typography.titleMedium)
                    mealPlan.forEach { meal ->
                        Text(meal, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

//fun generateMealPlan(): List<String> {
//    var apiResponseList = mapJsonResponse(JSON_DATA)
//    val mealPlan = mutableListOf<String>()
//
//    for (apiResponse in apiResponseList) {
//        // Access properties of each ApiResponse object
//        val day = apiResponse.Day
//        val breakfast = apiResponse.Breakfast
//        val lunch = apiResponse.Lunch
//        val dinner = apiResponse.Dinner
//
//        // Access properties of Breakfast, Lunch, and Dinner objects
//        val breakfastName = breakfast?.name
//        val breakfastIngredients = breakfast?.ingredients
//        val breakfastProcess = breakfast?.process
//        val breakfastMacros = breakfast?.macros
//
//        val lunchName = lunch?.name
//        val lunchIngredients = lunch?.ingredients
//        val lunchProcess = lunch?.process
//        val lunchMacros = lunch?.macros
//
//        val dinnerName = dinner?.name
//        val dinnerIngredients = dinner?.ingredients
//        val dinnerProcess = dinner?.process
//        val dinnerMacros = dinner?.macros
//
//        // Do something with the data
//        // println("Day: $day")
//    }
//
//    return emptyList()
//}
//
//fun mapJsonResponse(jsonString: String): List<ApiResponse> {
//    val gson = Gson()
//    val jsonArray = JsonParser.parseString(jsonString).asJsonArray
//    val apiResponseList = mutableListOf<ApiResponse>()
//
//    for (jsonElement in jsonArray) {
//        val apiResponse = gson.fromJson(jsonElement, ApiResponse::class.java)
//        apiResponseList.add(apiResponse)
//    }
//
//    return apiResponseList
//}

private const val JSON_DATA = """
    [
  {
    "Day": "Monday",
    "Breakfast": {
      "name": "Spicy Oatmeal with Berries and Nuts",
      "ingredients": "- 1/2 cup rolled oats\n- 1 cup milk (dairy or plant-based)\n- 1/4 teaspoon cinnamon\n- Pinch of cayenne pepper\n- 1/4 cup mixed berries\n- 2 tablespoons chopped nuts",
      "process": "1. Combine oats, milk, cinnamon, and cayenne pepper in a small saucepan.\n2. Cook over medium heat, stirring occasionally, until oats are cooked through and creamy.\n3. Top with berries and nuts.",
      "macros": "High protein, high fiber, moderate carbs"
    },
    "Lunch": {
      "name": "Spicy Chicken Breast with Roasted Vegetables",
      "ingredients": "- 1 boneless, skinless chicken breast\n- 1 cup broccoli florets\n- 1/2 cup chopped carrots\n- 1/2 cup bell pepper strips\n- 1 tablespoon olive oil\n- 1 teaspoon chili powder\n- 1/2 teaspoon cumin\n- 1/4 teaspoon garlic powder\n- 1/4 teaspoon onion powder\n- Salt and pepper to taste",
      "process": "1. Preheat oven to 400°F (200°C).\n2. In a bowl, combine olive oil, chili powder, cumin, garlic powder, onion powder, salt, and pepper.\n3. Coat chicken breast in the spice mixture.\n4. Place chicken breast and vegetables on a baking sheet.\n5. Roast for 20-25 minutes, or until chicken is cooked through and vegetables are tender.",
      "macros": "High protein, moderate carbs, low fat"
    },
    "Dinner": {
      "name": "Lentil Soup with Whole-Grain Bread",
      "ingredients": "- 1 cup lentils\n- 4 cups vegetable broth\n- 1/2 cup chopped carrots\n- 1/2 cup chopped celery\n- 1/2 onion, chopped\n- 1 teaspoon curry powder\n- 1/2 teaspoon cumin\n- 1/4 teaspoon turmeric\n- Salt and pepper to taste\n- 2 slices whole-grain bread",
      "process": "1. Rinse lentils and drain.\n2. In a large pot, combine lentils, vegetable broth, carrots, celery, onion, curry powder, cumin, turmeric, salt, and pepper.\n3. Bring to a boil, then reduce heat and simmer for 25-30 minutes, or until lentils are tender.\n4. Serve with whole-grain bread.",
      "macros": "High protein, high fiber, moderate carbs"
    }
  },
  {
    "Day": "Tuesday",
    "Breakfast": {
      "name": "Protein Smoothie with Spinach and Banana",
      "ingredients": "- 1 scoop protein powder\n- 1 cup spinach\n- 1 banana\n- 1/2 cup milk (dairy or plant-based)\n- 1/4 cup water\n- Pinch of cinnamon",
      "process": "1. Combine all ingredients in a blender and blend until smooth.",
      "macros": "High protein, moderate carbs, low fat"
    },
    "Lunch": {
      "name": "Salmon with Quinoa and Asparagus",
      "ingredients": "- 1 salmon fillet\n- 1/2 cup quinoa\n- 1 cup asparagus spears\n- 1/2 lemon\n- 1 tablespoon olive oil\n- 1/2 teaspoon paprika\n- 1/4 teaspoon garlic powder\n- Salt and pepper to taste",
      "process": "1. Cook quinoa according to package instructions.\n2. Preheat oven to 400°F (200°C).\n3. Place salmon fillet on a baking sheet.\n4. Drizzle with olive oil and squeeze lemon juice over the top.\n5. Sprinkle with paprika, garlic powder, salt, and pepper.\n6. Roast for 12-15 minutes, or until salmon is cooked through.\n7. Steam asparagus until tender-crisp.",
      "macros": "High protein, moderate carbs, healthy fats"
    },
    "Dinner": {
      "name": "Ground Turkey Stir-Fry with Brown Rice",
      "ingredients": "- 1 pound ground turkey\n- 1 cup brown rice\n- 2 cups mixed vegetables (such as broccoli, carrots, bell peppers)\n- 2 tablespoons soy sauce\n- 1 tablespoon sriracha\n- 1 teaspoon ginger\n- 1/2 teaspoon garlic powder",
      "process": "1. Cook brown rice according to package instructions.\n2. In a large skillet or wok, brown ground turkey over medium heat.\n3. Add vegetables and cook until tender-crisp.\n4. Stir in soy sauce, sriracha, ginger, and garlic powder.\n5. Serve over brown rice.",
      "macros": "High protein, moderate carbs, low fat"
    }
  },
  {
    "Day": "Wednesday",
    "Breakfast": {
      "name": "Spicy Tofu Scramble",
      "ingredients": "- 1 block firm tofu, crumbled\n- 1 tablespoon nutritional yeast\n- 1/2 teaspoon turmeric\n- 1/4 teaspoon chili powder\n- 1/4 cup chopped onion\n- 1/4 cup chopped bell pepper\n- Salt and pepper to taste",
      "process": "1. In a skillet, cook tofu over medium heat until browned.\n2. Add nutritional yeast, turmeric, chili powder, onion, bell pepper, salt, and pepper.\n3. Cook until vegetables are tender.",
      "macros": "High protein, moderate carbs, low fat"
    },
    "Lunch": {
      "name": "Lentil Soup with Whole-Grain Bread",
      "ingredients": "- Leftover lentil soup from Monday\n- 2 slices whole-grain bread",
      "process": "1. Reheat lentil soup.\n2. Serve with whole-grain bread.",
      "macros": "High protein, high fiber, moderate carbs"
    },
    "Dinner": {
      "name": "Tofu Scramble with Avocado Toast",
      "ingredients": "- 1 block firm tofu, crumbled\n- 1 tablespoon nutritional yeast\n- 1/2 teaspoon turmeric\n- 1/4 cup chopped onion\n- 1/4 cup chopped bell pepper\n- 1/4 cup chopped mushrooms\n- Salt and pepper to taste\n- 1 avocado, mashed\n- 2 slices whole-grain bread",
      "process": "1. In a skillet, cook tofu over medium heat until browned.\n2. Add nutritional yeast, turmeric, onion, bell pepper, mushrooms, salt, and pepper.\n3. Cook until vegetables are tender.\n4. Toast whole-grain bread and spread with mashed avocado.\n5. Top with tofu scramble.",
      "macros": "High protein, healthy fats, moderate carbs"
    }
  },
  {
    "Day": "Thursday",
    "Breakfast": {
      "name": "Greek Yogurt with Berries and Granola",
      "ingredients": "- 1 cup Greek yogurt\n- 1/4 cup mixed berries\n- 1/4 cup granola",
      "process": "1. Combine Greek yogurt, berries, and granola in a bowl.",
      "macros": "High protein, moderate carbs, low fat"
    },
    "Lunch": {
      "name": "Ground Turkey Stir-Fry with Brown Rice",
      "ingredients": "- Leftover ground turkey stir-fry from Tuesday\n- 1/2 cup brown rice",
      "process": "1. Reheat ground turkey stir-fry.\n2. Cook brown rice according to package instructions.\n3. Serve stir-fry over rice.",
      "macros": "High protein, moderate carbs, low fat"
    },
    "Dinner": {
      "name": "Chickpea Curry with Coconut Milk and Rice",
      "ingredients": "- 1 can chickpeas, drained and rinsed\n- 1 can coconut milk\n- 1 chopped tomato\n- 1/2 onion, chopped\n- 2 cloves garlic, minced\n- 1 tablespoon curry powder\n- 1 teaspoon garam masala\n- 1/2 teaspoon turmeric\n- Salt and pepper to taste\n- 1 cup rice",
      "process": "1. Cook rice according to package instructions.\n2. In a large pot, combine chickpeas, coconut milk, tomato, onion, garlic, curry powder, garam masala, turmeric, salt, and pepper.\n3. Bring to a boil, then reduce heat and simmer for 15-20 minutes, or until sauce has thickened.\n4. Serve over rice.",
      "macros": "High protein, high fiber, moderate carbs"
    }
  },
  {
    "Day": "Friday",
    "Breakfast": {
      "name": "Protein Pancakes",
      "ingredients": "- 1/2 cup protein pancake mix\n- Water or milk (dairy or plant-based) as needed\n- 1/4 cup mixed berries",
      "process": "1. Prepare protein pancake mix according to package instructions.\n2. Top with berries.",
      "macros": "High protein, moderate carbs, low fat"
    },
    "Lunch": {
      "name": "Tofu Scramble with Avocado Toast",
      "ingredients": "- Leftover tofu scramble from Wednesday\n- 1 avocado, mashed\n- 2 slices whole-grain bread",
      "process": "1. Reheat tofu scramble.\n2. Toast whole-grain bread and spread with mashed avocado.\n3. Top with tofu scramble.",
      "macros": "High protein, healthy fats, moderate carbs"
    },
    "Dinner": {
      "name": "Spicy Black Bean Burgers with Sweet Potato Fries",
      "ingredients": "- 1 can black beans, drained and rinsed\n- 1/2 cup oats\n- 1/2 teaspoon chili powder\n- 1/4 teaspoon cumin\n- 1/4 teaspoon garlic powder\n- Salt and pepper to taste\n- 2 sweet potatoes",
      "process": "1. Preheat oven to 400°F (200°C).\n2. Mash black beans in a bowl.\n3. Add oats, chili powder, cumin, garlic powder, salt, and pepper.\n4. Form mixture into patties.\n5. Bake or pan-fry patties until cooked through.\n6. Cut sweet potatoes into fries and toss with olive oil, salt, and pepper.\n7. Bake fries until tender and slightly crispy.",
      "macros": "High protein, high fiber, moderate carbs"
    }
  },
  {
    "Day": "Saturday",
    "Breakfast": {
      "name": "Chia Seed Pudding",
      "ingredients": "- 1/4 cup chia seeds\n- 1 cup milk (dairy or plant-based)\n- 1/4 teaspoon vanilla extract\n- 1/4 cup mixed berries",
      "process": "1. Combine chia seeds, milk, and vanilla extract in a jar or container.\n2. Stir well and refrigerate for at least 2 hours, or overnight.\n3. Top with berries.",
      "macros": "High protein, high fiber, moderate carbs"
    },
    "Lunch": {
      "name": "Chickpea Curry with Coconut Milk and Rice",
      "ingredients": "- Leftover chickpea curry from Thursday\n- 1/2 cup rice",
      "process": "1. Reheat chickpea curry.\n2. Cook rice according to package instructions.\n3. Serve curry over rice.",
      "macros": "High protein, high fiber, moderate carbs"
    },
    "Dinner": {
      "name": "Spicy Shrimp with Brown Rice and Broccoli",
      "ingredients": "- 1 pound shrimp, peeled and deveined\n- 1 tablespoon olive oil\n- 1 teaspoon chili powder\n- 1/2 teaspoon cumin\n- 1/4 teaspoon garlic powder\n- Salt and pepper to taste\n- 1 cup brown rice\n- 1 cup broccoli florets",
      "process": "1. Cook brown rice according to package instructions.\n2. Steam broccoli until tender-crisp.\n3. In a skillet, heat olive oil over medium heat.\n4. Add shrimp and cook until pink and cooked through.\n5. Sprinkle with chili powder, cumin, garlic powder, salt, and pepper.\n6. Serve shrimp with brown rice and broccoli.",
      "macros": "High protein, moderate carbs, low fat"
    }
  },
  {
    "Day": "Sunday",
    "Breakfast": {
      "name": "Scrambled Tofu with Vegetables",
      "ingredients": "- 1 block firm tofu, crumbled\n- 1/4 cup chopped onion\n- 1/4 cup chopped bell pepper\n- 1/4 cup chopped mushrooms\n- Salt and pepper to taste",
      "process": "1. In a skillet, cook tofu over medium heat until browned.\n2. Add onion, bell pepper, mushrooms, salt, and pepper.\n3. Cook until vegetables are tender.",
      "macros": "High protein, moderate carbs, low fat"
    },
    "Lunch": {
      "name": "Spicy Black Bean Burgers with Sweet Potato Fries",
      "ingredients": "- Leftover black bean burgers from Friday\n- Leftover sweet potato fries from Friday",
      "process": "1. Reheat black bean burgers and sweet potato fries.",
      "macros": "High protein, high fiber, moderate carbs"
    },
    "Dinner": {
      "name": "Salmon with Roasted Brussels Sprouts",
      "ingredients": "- 1 salmon fillet\n- 1 pound Brussels sprouts, halved\n- 1 tablespoon olive oil\n- 1/2 teaspoon paprika\n- 1/4 teaspoon garlic powder\n- Salt and pepper to taste",
      "process": "1. Preheat oven to 400°F (200°C).\n2. Toss Brussels sprouts with olive oil, paprika, garlic powder, salt, and pepper.\n3. Place salmon fillet and Brussels sprouts on a baking sheet.\n4. Roast for 20-25 minutes, or until salmon is cooked through and Brussels sprouts are tender.",
      "macros": "High protein, moderate carbs, healthy fats"
    }
  }
]
"""