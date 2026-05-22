package com.muhammad.nutribot.presentation.screens.meal_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.R
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.presentation.theme.CarbsColor
import com.muhammad.nutribot.presentation.theme.FatColor
import com.muhammad.nutribot.presentation.theme.ProteinColor

@Composable
fun MealNutritionSection(
    modifier: Modifier = Modifier,
    food: Food,
    onProteinClick: () -> Unit,
    onFatClick: () -> Unit,
    onCarbsClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MealNutritionCard(
            icon = R.drawable.ic_protein,
            color = ProteinColor,
            modifier = Modifier.weight(1f),
            label = R.string.protein,
            value = food.protein, onClick = onProteinClick
        )
        MealNutritionCard(
            icon = R.drawable.ic_fat,
            color = FatColor,
            modifier = Modifier.weight(1f),
            label = R.string.fat,
            value = food.fat, onClick = onFatClick
        )
        MealNutritionCard(
            icon = R.drawable.ic_carbs,
            color = CarbsColor,
            modifier = Modifier.weight(1f),
            label = R.string.carbs,
            value = food.carbs, onClick = onCarbsClick
        )
    }
}