plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.milsabores"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.milsabores"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

    // 2. Añade el bloque composeOptions
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}
dependencies {

    // --- Core y ViewModel ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose) // <-- ViewModel

    // --- Compose (BOM) ---
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    // --- Navegación ---
    implementation(libs.androidx.navigation.compose) // <-- Navegación

    // --- Base de Datos (Room) ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)      // Soporte para Coroutines
    ksp(libs.androidx.room.compiler)            // El procesador de anotaciones

    // --- Carga de Imágenes (Coil) ---
    implementation(libs.coil.compose) // <-- Imágenes

    // --- Permisos Nativos (Accompanist) ---
    implementation(libs.accompanist.permissions) // <-- Permisos


    // --- Pruebas (Testing) ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // --- RED (Según pauta) ---
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi) // Usamos Moshi
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp.logging) // Para ver los logs en consola

    // --- CÁMARA ---
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // --- TESTING ---
    testImplementation(libs.truth) // Google Truth
    testImplementation(libs.mockwebserver) // Para simular API
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.turbine)
}