package com.edurocks.recipes.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.edurocks.recipes.R
import com.edurocks.recipes.domain.model.Recipe
import com.edurocks.recipes.util.loadPicture
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun RecipeView(
    recipe: Recipe
) {
    ScrollableColumn(modifier = Modifier.fillMaxWidth()) {
        recipe.featuredImage?.let { url ->
            val image = loadPicture(url = url, defaultImage = R.drawable.empty_plate)
            image.value?.let { img ->
                Image(
                    bitmap = img.asImageBitmap(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .preferredHeight(260.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                recipe.title?.let { title ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)) {
                        Text(
                            text = title,
                            modifier =  Modifier
                                .fillMaxWidth(0.85f)
                                .wrapContentWidth(Alignment.Start),
                            style = MaterialTheme.typography.h3
                        )
                        val rank = recipe.rating.toString()
                        Text(
                            text = rank,
                            modifier =  Modifier
                                .fillMaxWidth(0.85f)
                                .wrapContentWidth(Alignment.End)
                                .align(Alignment.CenterVertically),
                            style = MaterialTheme.typography.h5
                        )
                    }
                }
                recipe.publisher?.let { publisher ->
                    val updated = recipe.dateUpdated
                    Text(
                        text = if (updated != null) {
                            "Updated $updated by $publisher"
                        } else {
                            "Updated by $publisher"
                        },
                        modifier =  Modifier
                            .fillMaxWidth(0.85f)
                            .padding(4.dp),
                        style = MaterialTheme.typography.caption
                    )
                }
                for (ingredient in recipe.ingredients) {
                    Text(
                        text = ingredient,
                        modifier =  Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}