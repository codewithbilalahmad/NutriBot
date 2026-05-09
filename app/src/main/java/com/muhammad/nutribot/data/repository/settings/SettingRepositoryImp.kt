package com.muhammad.nutribot.data.repository.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.muhammad.nutribot.domain.model.NutritionCalculation
import com.muhammad.nutribot.domain.model.UserProfile
import com.muhammad.nutribot.domain.repository.settings.SettingRepository
import com.muhammad.nutribot.utils.Constants.DATA_STORE_FILE_NAME
import com.muhammad.nutribot.utils.Constants.IS_USER_LOGGED_IN_PREF_KEY
import com.muhammad.nutribot.utils.Constants.NUTRITION_CALCULATION_PREF_KEY
import com.muhammad.nutribot.utils.Constants.USER_PROFILE_PREF_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.lang.Exception

class SettingRepositoryImp(
    private val context : Context
) : SettingRepository{
    companion object{
        private val Context.prefs by preferencesDataStore(DATA_STORE_FILE_NAME)
        private val USER_PROFILE_KEY = stringPreferencesKey(USER_PROFILE_PREF_KEY)
        private val NUTRITION_CALCULATION_KEY = stringPreferencesKey(NUTRITION_CALCULATION_PREF_KEY)
        private val IS_USER_LOGGED_IN_KEY = booleanPreferencesKey(IS_USER_LOGGED_IN_PREF_KEY)
    }
    override suspend fun saveUserProfile(userProfile: UserProfile) {
        context.prefs.edit {prefs ->
            val json = Json.encodeToString<UserProfile>(userProfile)
            prefs[USER_PROFILE_KEY] = json
        }
    }

    override suspend fun saveNutritionCalculation(nutritionCalculation: NutritionCalculation) {
        context.prefs.edit {prefs ->
            val json = Json.encodeToString<NutritionCalculation>(nutritionCalculation)
            prefs[NUTRITION_CALCULATION_KEY] = json
        }
    }

    override suspend fun saveIsUserLoggedIn(loggedIn: Boolean) {
        context.prefs.edit {prefs ->
            prefs[IS_USER_LOGGED_IN_KEY] = loggedIn
        }
    }

    override fun observeUserProfile(): Flow<UserProfile?> {
        return context.prefs.data.map { prefs ->
            val json = prefs[USER_PROFILE_KEY]
            json?.let {
                try {
                    Json.decodeFromString<UserProfile>( it)
                } catch (e : Exception){
                    e.printStackTrace()
                    null
                }
            }
        }.distinctUntilChanged()
    }

    override fun observeNutritionCalculation(): Flow<NutritionCalculation?> {
        return context.prefs.data.map { prefs ->
            val json = prefs[NUTRITION_CALCULATION_KEY]
            json?.let {
                try {
                    Json.decodeFromString<NutritionCalculation>(it)
                } catch (e : Exception){
                    e.printStackTrace()
                    null
                }
            }
        }.distinctUntilChanged()
    }

    override fun observeIsUserLoggedIn(): Flow<Boolean> {
        return context.prefs.data.map { prefs ->
            prefs[IS_USER_LOGGED_IN_KEY] ?: false
        }.distinctUntilChanged()
    }
}