plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.smarttech.store"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.smarttech.store"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    // Firebase: administración centralizada de versiones
    implementation(platform(libs.firebase.bom))

// Autenticación de usuarios
    implementation(libs.firebase.auth)

// Base de datos en la nube
    implementation(libs.firebase.firestore)

// Almacenamiento de imágenes
    implementation(libs.firebase.storage)

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("androidx.cardview:cardview:1.0.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

}