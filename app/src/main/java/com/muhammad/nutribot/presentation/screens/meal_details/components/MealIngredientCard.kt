package com.muhammad.nutribot.presentation.screens.meal_details.components

import com.muhammad.nutribot.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.domain.model.Ingredient
import com.muhammad.nutribot.presentation.components.checkbox.AppCheckBox
import com.muhammad.nutribot.presentation.theme.CarbsColor
import com.muhammad.nutribot.presentation.theme.FatColor
import com.muhammad.nutribot.presentation.theme.ProteinColor
import com.muhammad.nutribot.utils.rippleClickable

@Composable
fun MealIngredientCard(modifier: Modifier = Modifier, ingredient: Ingredient, onClick: () -> Unit) {
    Card(
        modifier = modifier.rippleClickable(onClick = onClick).dropShadow(
            shape = RoundedCornerShape(16.dp),
            shadow = Shadow(
                radius = 2.dp,
                spread = 2.dp,
                color = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = ingredient.name, style = MaterialTheme.typography.bodyLarge)
                AppCheckBox(checked = ingredient.isSelected, onCheckChange = {
                    onClick()
                }, modifier = Modifier.size(24.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IngredientItem(
                    icon = R.drawable.ic_calories,
                    label = "${ingredient.calories} ${stringResource(R.string.kcal)}",
                    color = MaterialTheme.colorScheme.primary
                )
                IngredientItem(
                    icon = R.drawable.ic_protein,
                    label = "${ingredient.protein} ${stringResource(R.string.grams)}",
                    color = ProteinColor
                )
                IngredientItem(
                    icon = R.drawable.ic_fat,
                    label = "${ingredient.fat} ${stringResource(R.string.grams)}",
                    color = FatColor
                )
                IngredientItem(
                    icon = R.drawable.ic_carbs,
                    label = "${ingredient.carbs} ${stringResource(R.string.grams)}",
                    color = CarbsColor
                )
            }
        }
    }
}

@Composable
fun IngredientItem(modifier: Modifier = Modifier, icon: Int, label: String, color: Color) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}