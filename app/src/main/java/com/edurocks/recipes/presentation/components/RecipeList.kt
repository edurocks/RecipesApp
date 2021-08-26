package com.edurocks.recipes.presentation.components

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edurocks.recipes.R
import com.edurocks.recipes.domain.model.Recipe
import com.edurocks.recipes.presentation.ui.recipe_list.PAGE_SIZE

@ExperimentalMaterialApi
@Composable
fun RecipeList(
    loading: Boolean,
    recipes: List<Recipe>,
    onChangeScrollPosition: (Int) -> Unit,
    page: Int,
    onTriggerNextPage: () -> Unit,
    navController: NavController,
    scaffoldState: ScaffoldState
){
    // To show the progress bar with overlay before the list comes up
    Box(modifier = Modifier.fillMaxSize()) {

        if (loading && recipes.isEmpty()) {
            //If we want the shimmer effect
            LoadingRecipeListShimmer(imageHeight = 250.dp)
        } else {
            //Recipes List
            LazyColumn {
                itemsIndexed(items = recipes) { index, recipe ->
                    onChangeScrollPosition(index)
                    if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                        onTriggerNextPage()
                    }
                    RecipeCard(recipe = recipe,
                        onClick = {
                            val bundle = Bundle()
                            recipe.id?.let { bundle.putInt("recipeId", it) }
                            navController.navigate(R.id.viewRecipe, bundle)
                        }
                    )
                }
            }
        }

        CircularIndeterminateProgressBar(isDisplayed = loading)

        DefaultSnackbar(
            snackbarHostState = scaffoldState.snackbarHostState,
            onDismiss = {
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}