package com.pjasoft.recipeapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pjasoft.recipeapp.data.KtorfitClient
import com.pjasoft.recipeapp.domain.dtos.Prompt
import com.pjasoft.recipeapp.domain.dtos.RecipeDTO
import com.pjasoft.recipeapp.domain.models.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    val userId = 2
    val recipeService = KtorfitClient.createRecipeService()
    var recipes by mutableStateOf<List<Recipe>>(listOf())
    var generatedRecipe by mutableStateOf<RecipeDTO?>(null)
    var showSheet by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    init {
        getRecipes()
    }

    fun generateRecipe(prompt: Prompt){
        viewModelScope.launch {

            try {
                val result = recipeService.generateRecipe(prompt)
                println(result.toString())
            }catch (e: Exception){
                println(e.toString())
            }
        }
    }

    fun getRecipes(){
        viewModelScope.launch {
            try {
                val result = recipeService.getRecipesByUserId(userId)
                recipes = result.takeLast(5).reversed()
                println(result.toString())
            }catch (e: Exception){
                println(e.toString())
            }
        }
    }
}