package com.muhammad.nutribot.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.muhammad.nutribot.NutriBotApplication
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import java.io.File
import java.io.FileOutputStream

val MEAL_LABELS = listOf(
    "food",
    "meal",
    "dish",
    "plate",
    "breakfast",
    "lunch",
    "dinner",
    "snack",
    "dessert",
    "pizza",
    "burger",
    "sandwich",
    "rice",
    "fried rice",
    "biryani",
    "pasta",
    "spaghetti",
    "noodles",
    "ramen",
    "soup",
    "salad",
    "steak",
    "beef",
    "chicken",
    "fish",
    "seafood",
    "egg",
    "omelette",
    "sausage",
    "fries",
    "potato",
    "bread",
    "toast",
    "cake",
    "ice cream",
    "cookie",
    "fruit",
    "banana",
    "apple",
    "orange",
    "watermelon",
    "vegetable",
    "coffee",
    "tea",
    "juice",
    "drink"
)

fun decodeBitmap(
    path: String,
    reqWidth: Int = 300,
    reqHeight: Int = 300
): Bitmap? {
    val context = NutriBotApplication.INSTANCE
    return try {
        val isUri = path.startsWith("content://") || path.startsWith("file://")

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        if (isUri) {
            val uri = path.toUri()
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            }
        } else {
            BitmapFactory.decodeFile(path, options)
        }

        if (options.outWidth <= 0 || options.outHeight <= 0) {
            return null
        }

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        return if (isUri) {
            val uri = path.toUri()
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            }
        } else {
            BitmapFactory.decodeFile(path, options)
        }

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int,
): Int {
    val (height: Int, width: Int) = options.outHeight to options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}


fun detectImageContainsMeal(bitmap: Bitmap, onResult: (Boolean) -> Unit) {
    val image = InputImage.fromBitmap(bitmap, 0)
    val options = ImageLabelerOptions.Builder()
        .setConfidenceThreshold(0.5f)
        .build()
    val labeler = ImageLabeling.getClient(options)

    labeler.process(image)
        .addOnSuccessListener { labels ->
            val isMeal = labels.any { label ->
                MEAL_LABELS.any { carLabel ->
                    label.text.equals(carLabel, ignoreCase = true)
                }
            }
            onResult(isMeal)
            labeler.close()
        }
        .addOnFailureListener {
            it.printStackTrace()
            onResult(false)
            labeler.close()
        }
}

fun LocalDate.startOfDayMillis(): Long {
    return atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun LocalDate.endOdDayMillis(): Long {
    return plus(1, DateTimeUnit.DAY).atStartOfDayIn(TimeZone.currentSystemDefault())
        .toEpochMilliseconds() - 1
}

fun getWeekDatesFrom(date: LocalDate): List<LocalDate> {
    val startOfWeek = date.minus(date.dayOfWeek.ordinal, DateTimeUnit.DAY)
    return (0..6).map { day ->
        startOfWeek.plus(day, DateTimeUnit.DAY)
    }
}

fun cleanJson(raw: String): String {
    val start = raw.indexOf("{")
    val end = raw.lastIndexOf("}")

    return if (start != -1 && end != -1) {
        raw.substring(start, end + 1)
    } else {
        raw
    }
}

fun checkPermissionGranted(context: Context, permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun openPermissionSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}

fun saveBitmapToFile(bitmap : Bitmap, prefix : String) : String{
    val context = NutriBotApplication.INSTANCE
    val file = File(context.cacheDir, "${prefix}_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    }
    return file.absolutePath
}

@SuppressLint("UseKtx")
fun resizeBitmap(bitmap: Bitmap): Bitmap {
    val maxSize = 512
    val ratio = minOf(
        maxSize.toFloat() / bitmap.width,
        maxSize.toFloat() / bitmap.height
    )
    val width = (bitmap.width * ratio).toInt()
    val height = (bitmap.height * ratio).toInt()
    return Bitmap.createScaledBitmap(bitmap, width, height, true)
}