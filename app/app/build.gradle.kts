import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.example.one_tap_sign_in"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.one_tap_sign_in"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false

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

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.collections.immutable)

    // Android Core
    implementation(libs.androidx.core.ktx)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Jetpack Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    // UI
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Material Design
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    // Coil (Image loading)
    implementation(libs.coil.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Koin (DI)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.workmanager)

    // DataStore (Preferences file)
    implementation(libs.androidx.datastore)

    // HTTP Client
    implementation(libs.ktor.core)
    implementation(libs.ktor.engine)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.logging)

    // Auth
    implementation(libs.credentials.core)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.google.identity)

    // Serialization
    implementation(libs.kotlin.serialization.json)

    // Background work
    implementation(libs.workmanager.runtime)

    // Testing
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.workmanager.testing)
    androidTestImplementation(libs.kluent.android)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.mockk.agent)

    // Other
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
