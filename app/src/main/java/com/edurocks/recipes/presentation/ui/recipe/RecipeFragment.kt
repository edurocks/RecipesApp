package com.edurocks.recipes.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.edurocks.recipes.presentation.BaseApplication
import com.edurocks.recipes.presentation.components.CircularIndeterminateProgressBar
import com.edurocks.recipes.presentation.components.LoadingRecipeShimmer
import com.edurocks.recipes.presentation.components.RecipeView
import com.edurocks.recipes.presentation.theme.AppTheme
import com.edurocks.recipes.presentation.ui.recipe.RecipeEvent.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipeFragment: Fragment() {

    @Inject
    lateinit var application: BaseApplication

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("recipeId")?.let { recipeId ->
            viewModel.onTriggerEvent(GetRecipeEvent(recipeId))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply{
            setContent {

                val loading = viewModel.loading.value

                val recipe = viewModel.recipe.value

                AppTheme(darkTheme = application.isDark.value) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (loading && recipe == null) {
                            LoadingRecipeShimmer(imageHeight = 260.dp)
                        } else {
                            recipe?.let { recipe ->
                                RecipeView(recipe = recipe)
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.Center) {
                        CircularIndeterminateProgressBar(isDisplayed = loading)
                    }
                }
            }
        }
    }
}









