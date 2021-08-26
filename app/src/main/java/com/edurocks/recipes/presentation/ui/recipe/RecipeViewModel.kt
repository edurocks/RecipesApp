package com.edurocks.recipes.presentation.ui.recipe

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurocks.recipes.domain.model.Recipe
import com.edurocks.recipes.presentation.ui.recipe.RecipeEvent.*
import com.edurocks.recipes.repository.RecipeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_RECIPE = "recipe.state.recipe.key"

@ExperimentalCoroutinesApi
class RecipeViewModel
@ViewModelInject @Inject constructor(private val recipeRepository: RecipeRepository,
                    private @Named("auth_token") val token: String,
                    @Assisted private val state: SavedStateHandle) : ViewModel() {

    val recipe: MutableState<Recipe?> = mutableStateOf(null)
    val loading = mutableStateOf(false)

    init {
        // restore if process dies
        state.get<Int>(STATE_KEY_RECIPE)?.let { recipeId ->
            onTriggerEvent(GetRecipeEvent(recipeId))
        }
    }

    fun onTriggerEvent(event: RecipeEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is GetRecipeEvent -> {
                        getRecipe(event.id)
                    }
                }
            }catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    private suspend fun getRecipe(id: Int) {
        loading.value = true

        val recipe = recipeRepository.get(
            token = token,
            id = id
        )
        this.recipe.value = recipe
        state.set(STATE_KEY_RECIPE, recipe.id)

        loading.value = false
    }
}