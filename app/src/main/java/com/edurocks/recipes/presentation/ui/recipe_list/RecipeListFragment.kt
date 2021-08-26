package com.edurocks.recipes.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.edurocks.recipes.presentation.BaseApplication
import com.edurocks.recipes.presentation.components.*
import com.edurocks.recipes.presentation.theme.AppTheme
import com.edurocks.recipes.presentation.ui.recipe_list.RecipeListEvent.*
import com.edurocks.recipes.util.SnackbarController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalMaterialApi
@AndroidEntryPoint
class RecipeListFragment: Fragment() {

    @Inject
    lateinit var application: BaseApplication

    private val viewModel: RecipeListViewModel by viewModels()

    private val snackbarController = SnackbarController(lifecycleScope)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                AppTheme(darkTheme = application.isDark.value) {
                    val recipes = viewModel.recipes.value

                    val query = viewModel.query.value

                    val selectedCategory = viewModel.selectedCategory.value

                    val categoryScrollPosition = viewModel.categoryScrollPosition

                    val loading = viewModel.loading.value

                    val scaffoldState = rememberScaffoldState()

                    val page = viewModel.page.value

                    //Use when we want to have a top bar, bottom bar, nav drawer, snackbar, etc...
                    Scaffold(
                        topBar = {
                            //Top Toolbar And Chips
                            SearchAppBar(
                                query = query,
                                onQueryChanged = viewModel::onQueryChanged,
                                onExecuteSearch = {
                                   //Just an example to show a snackbar when milk category is selected
                                   if (viewModel.selectedCategory.value?.value == "Milk") {
                                       snackbarController.getScope().launch {
                                           snackbarController.showSnackbar(
                                               scaffoldState = scaffoldState,
                                               message = "Invalid category: MILK",
                                               actionLabel = "Hide"
                                           )
                                       }
                                   } else {
                                       viewModel.onTriggerEvent(NewSearchEvent)
                                   }
                                },
                                selectedCategory = selectedCategory,
                                onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                                scrollPosition = categoryScrollPosition,
                                onChangeScrollPosition = viewModel::onChangeCategoryScrollPosition,
                                onToogleTheme = {
                                    application.toogleLightTheme()
                                }
                            )
                        },
                        scaffoldState = scaffoldState,
                        snackbarHost = {
                            scaffoldState.snackbarHostState
                        }
                    ) {
                        RecipeList(
                            loading = loading,
                            recipes = recipes,
                            onChangeScrollPosition = viewModel::onChangeRecipeScrollPosition,
                            page = page,
                            onTriggerNextPage = { viewModel.onTriggerEvent(NextPageEvent) },
                            navController = findNavController(),
                            scaffoldState = scaffoldState
                        )
                    }
                }
            }
        }
    }
}





















