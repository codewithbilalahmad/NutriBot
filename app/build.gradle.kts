import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.muhammad.nutribot"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.muhammad.nutribot"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "API_KEY",
            "\"${project.findProperty("API_KEY")}\""
        )
        buildConfigField(
            "String",
            "FOOD_SEARCH_BASE_URL",
            "\"${project.findProperty("FOOD_SEARCH_BASE_URL")}\""
        )
        buildConfigField(
            "String",
            "BARCODE_MEAL_BASE_URL",
            "\"${project.findProperty("BARCODE_MEAL_BASE_URL")}\""
        )
        buildConfigField(
            "String",
            "FOOD_SEARCH_API_KEY",
            "\"${project.findProperty("FOOD_SEARCH_API_KEY")}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.lottie.compose)
    implementation(libs.bundles.ktor)
    implementation(libs.work.runtime.ktx)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.image.labeling)
    implementation(libs.generativeai)
    implementation(libs.navigation)
    implementation(libs.kotlinx.datetime)
    implementation(libs.datastore)
    implementation(libs.room.runtime)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.ui.graphics)
    "baselineProfile"(project(":baselineprofile"))
    ksp(libs.room.compiler)
    implementation(libs.splashscreen)
    implementation(libs.koin.compose)
    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.coil.compose)
    implementation(libs.mlkit.barcode)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

room {
    schemaDirectory("$projectDir/schemas")
}