package com.muhammad.nutribot.presentation.screens.meal_details

import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.nutribot.domain.model.EditMealOption
import com.muhammad.nutribot.domain.model.Food
import com.muhammad.nutribot.domain.repository.food.FoodRepository
import com.muhammad.nutribot.domain.repository.ingredient.IngredientRepository
import com.muhammad.nutribot.utils.decodeBitmap
import com.muhammad.nutribot.utils.saveBitmapToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class MealDetailViewModel(
    saveStateHandle: SavedStateHandle,
    private val foodRepository: FoodRepository,
    private val ingredientRepository: IngredientRepository,
) : ViewModel() {
    private val food =
        Json.decodeFromString<Food>(Uri.decode(checkNotNull(saveStateHandle["food"])))
    private val _state = MutableStateFlow(MealDetailState(food = food))
    val state = _state.asStateFlow()
    private val _events = Channel<MealDetailEvent>()
    val events = _events.receiveAsFlow()
    fun onAction(action: MealDetailAction) {
        when (action) {
            is MealDetailAction.OnChangeSelectedDate -> onChangeSelectedDate(action.date)
            MealDetailAction.OnToggleDatePickerDialog -> onToggleDatePickerDialog()
            is MealDetailAction.OnToggleIngredientSelection -> onToggleIngredientSelection(action.id)
            is MealDetailAction.OnToggleMealFavourite -> onToggleMealFavourite(action.favourite)
            is MealDetailAction.OnChangeMealNumberOfServings -> onChangeMealNumberOfServings(action.numberOfServings)
            MealDetailAction.OnLogToMeal -> onLogToMeal()
            MealDetailAction.OnDismissEditMealDialog -> onDismissEditMealDialog()
            is MealDetailAction.OnEditMealOptionClick -> onEditMealOptionClick(action.option)
            MealDetailAction.OnSaveEditMealOption -> onSaveEditMealOption()
            MealDetailAction.OnClearEditMealState -> onClearEditMealState()
            is MealDetailAction.OnSelectMealImage -> onSelectMealImage(action.uri)
        }
    }

    private fun onSelectMealImage(uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = decodeBitmap(path = uri) ?: return@launch
            val imageUrl = saveBitmapToFile(bitmap = bitmap, prefix = "meal_image")
            withContext(Dispatchers.Main) {
                _state.update { currentState ->
                    currentState.copy(
                        food = currentState.food.copy(
                            mealImageUrl = imageUrl
                        )
                    )
                }
            }
        }
    }

    private fun onClearEditMealState() {
        _state.update { it.copy(editMealState = TextFieldState("")) }
    }

    private fun onSaveEditMealOption() {
        _state.update { currentState ->
            val food = currentState.food
            val editMealValue = currentState.editMealState.text.toString()
            val selectedEditMealOption = currentState.selectedEditMealOption
            val newFood = when (selectedEditMealOption) {
                EditMealOption.NONE -> food
                EditMealOption.NAME -> food.copy(name = editMealValue)
                EditMealOption.CALORIES -> food.copy(calories = editMealValue.toInt())
                EditMealOption.PROTEIN -> food.copy(protein = editMealValue.toInt())
                EditMealOption.FATS -> food.copy(fat = editMealValue.toInt())
                EditMealOption.CARBS -> food.copy(carbs = editMealValue.toInt())
            }
            currentState.copy(
                showEditMealDialog = false,
                editMealState = TextFieldState(),
                selectedEditMealOption = EditMealOption.NONE,
                food = newFood
            )
        }
    }

    private fun onEditMealOptionClick(option: EditMealOption) {
        _state.update { currentState ->
            val editMealValue = when (option) {
                EditMealOption.NONE -> TextFieldState("")
                EditMealOption.NAME -> TextFieldState(currentState.food.name)
                EditMealOption.CALORIES -> TextFieldState(currentState.food.calories.toString())
                EditMealOption.PROTEIN -> TextFieldState(currentState.food.protein.toString())
                EditMealOption.FATS -> TextFieldState(currentState.food.fat.toString())
                EditMealOption.CARBS -> TextFieldState(currentState.food.carbs.toString())
            }
            currentState.copy(
                selectedEditMealOption = option,
                editMealState = editMealValue,
                showEditMealDialog = true
            )
        }
    }

    private fun onDismissEditMealDialog() {
        _state.update {
            it.copy(
                showEditMealDialog = false,
                editMealState = TextFieldState(),
                selectedEditMealOption = EditMealOption.NONE
            )
        }
    }

    private fun onLogToMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            val food = state.value.food
            val isAlreadyLogged = state.value.isAlreadyLogged
            if (isAlreadyLogged) {
                foodRepository.upsertFood(food = food)
            } else {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                val selectedDateTime = LocalDateTime(
                    date = state.value.selectedDate,
                    time = now.time
                )
                val eatenAt = selectedDateTime.toInstant(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
                foodRepository.upsertFood(food = food.copy(eatenAt = eatenAt))
                food.ingredients.forEach { ingredient ->
                    ingredientRepository.upsertIngredient(ingredient = ingredient)
                }
            }
            withContext(Dispatchers.Main) {
                _events.send(MealDetailEvent.OnMealLoggedSuccess)
            }
        }
    }

    private fun onChangeMealNumberOfServings(numberOfServings: Int) {
        _state.update { currentState ->
            currentState.copy(
                food = currentState.food.copy(
                    numberOfServings = numberOfServings,
                    calories = food.calories * numberOfServings,
                    protein = food.protein * numberOfServings,
                    fat = food.fat * numberOfServings,
                    carbs = food.carbs * numberOfServings
                )
            )
        }
    }

    private fun onToggleMealFavourite(favourite: Boolean?) {
        val food = _state.value.food
        _state.update { it.copy(food = food.copy(isFavorite = favourite ?: !food.isFavorite)) }
        if (state.value.isAlreadyLogged) {
            viewModelScope.launch(Dispatchers.IO) {
                foodRepository.updateFoodFavourite(id = food.id, isFavourite = food.isFavorite)
            }
        }
    }

    private fun onToggleIngredientSelection(id: Long) {
        _state.update { currentState ->
            currentState.copy(
                food = currentState.food.copy(
                    ingredients = currentState.food.ingredients.map { ingredient ->
                        if (ingredient.id == id) {
                            ingredient.copy(isSelected = !ingredient.isSelected)
                        } else ingredient
                    }
                ))
        }
    }

    private fun onToggleDatePickerDialog() {
        _state.update { it.copy(showDatePickerDialog = !it.showDatePickerDialog) }
    }

    private fun onChangeSelectedDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
    }
}