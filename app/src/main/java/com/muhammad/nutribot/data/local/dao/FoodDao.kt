package com.muhammad.nutribot.data.local.dao

import androidx.room.*
import com.muhammad.nutribot.data.local.entity.FoodEntity
import com.muhammad.nutribot.data.local.relations.FoodWithWithIngredients
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Dao
interface FoodDao{
    @Upsert
    suspend fun upsertFood(food: FoodEntity)
    @Query(
        "DELETE FROM FoodEntity WHERE id = :id"
    )
    fun deleteFoodById(id : Long)
    @Query(
        """
            SELECT * FROM FoodEntity
            WHERE eatenAt BETWEEN :startOfDay AND :endOfDay
            ORDER BY eatenAt DESC
            """
    )
    fun getFoodsByDate(
        startOfDay : Long,
       endOfDay : Long,
    ): Flow<List<FoodEntity>>
    @Query(
        """
            SELECT DISTINCT date(eatenAt / 100,'unixepoch')
            FROM FoodEntity
            ORDER BY eatenAt DESC
        """
    )
     fun getFoodDates() : Flow<List<String>>
    @Query(
        "SELECT * FROM FoodEntity WHERE id =:id ORDER BY eatenAt DESC"
    )
    fun getFoodById(id: Long): Flow<FoodWithWithIngredients?>
}

fun FoodDao.getFoodStreak(): Flow<Int> {
    return getFoodDates().map { dates ->

        if (dates.isEmpty()) return@map 0

        val localDates = dates
            .map { LocalDate.parse(it) }
            .sortedDescending()

        val today = Clock.System.todayIn(
            TimeZone.currentSystemDefault()
        )

        var streak = 0
        var expectedDate = today

        for (date in localDates) {

            if (date == expectedDate) {
                streak++
                expectedDate = expectedDate.minus(
                    1,
                    DateTimeUnit.DAY
                )
            } else if (date < expectedDate) {
                break
            }
        }

        streak
    }
}