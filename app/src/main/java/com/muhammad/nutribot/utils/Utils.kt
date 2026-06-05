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
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.muhammad.nutribot.NutriBotApplication
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.time.Clock

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
    reqHeight: Int = 300,
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
    return raw
        .replace("```json", "")
        .replace("```", "")
        .trim()
        .let {
            val start = it.indexOf("{")
            val end = it.lastIndexOf("}")
            if (start != -1 && end != -1 && end > start) {
                it.substring(start, end + 1)
            } else {
                it
            }
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

fun saveBitmapToFile(bitmap: Bitmap, prefix: String): String {
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

fun generateId(): Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE

fun LocalTime.toFormattedTime(): String {
    val amPm = if(hour > 12) "Pm" else "Am"
    val hour = if (hour > 12) hour - 12 else hour
    return "${"%02d:%02d".format(hour, minute)} $amPm"
}

fun getCurrentWeekDates(): List<LocalDate> {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val daysFromMonday = today.dayOfWeek.isoDayNumber - 1
    val monday = today.minus(daysFromMonday, DateTimeUnit.DAY)
    return (0..6).map { index ->
        monday.plus(index, DateTimeUnit.DAY)
    }
}

fun getCurrentWeekMillis(): Pair<Long, Long> {
    val timeZone = TimeZone.currentSystemDefault()
    val today = Clock.System.now()
        .toLocalDateTime(timeZone)
        .date
    val monday = today.minus(today.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
    val sunday = monday.plus(6, DateTimeUnit.DAY)
    val startOfWeek = monday.atStartOfDayIn(timeZone).toEpochMilliseconds()
    val endOfWeek = sunday.atStartOfDayIn(timeZone).toEpochMilliseconds()
    return startOfWeek to endOfWeek
}

fun String.isNetworkUrl(): Boolean {
    return startsWith("http://") || startsWith("https://")
}
fun scanBarcodeFromBitmap(
    bitmap: Bitmap,
    onSuccess : (String) -> Unit,
    onFailure : () -> Unit
){
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E
        )
        .build()

    val scanner = BarcodeScanning.getClient(options)
    val image = InputImage.fromBitmap(bitmap, 0)
    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            val barcode = barcodes.firstOrNull()?.rawValue

            if (!barcode.isNullOrEmpty()) {
                onSuccess(barcode)
            } else {
                onFailure()
            }
        }
        .addOnFailureListener{
            onFailure()
        }
}
