package com.muhammad.nutribot.presentation.navigation

import android.net.Uri
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.muhammad.nutribot.domain.model.Food
import kotlinx.serialization.json.Json

object CustomNavTypes {
    val Food = object : NavType<Food>(isNullableAllowed = false) {

        override val name: String
            get() = "Food"

        override fun put(bundle: SavedState, key: String, value: Food) {
            bundle.putString(key, Uri.encode(Json.encodeToString(value)))
        }

        override fun get(bundle: SavedState, key: String): Food? {
            return bundle.getString(key)?.let {
                Json.decodeFromString(Uri.decode(it))
            }
        }

        override fun parseValue(value: String): Food {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Food): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }
}