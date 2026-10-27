plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("kotlin-kapt")
}

android {
    namespace = "com.example.holamundo1"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.holamundo1"
        minSdk = 25
        targetSdk = 36
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
        compose = true
    }
}

dependencies {
    // --- LIBRERÍAS BASE DE ANDROID Y COMPOSE ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.8.2")

    // --- COIL, NAVEGACIÓN Y LIFECYCLE ---
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
    implementation("androidx.compose.material3:material3-window-size-class")

    // --- ROOM DATABASE Y DATASTORE ---
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // --- DEPENDENCIAS GUÍA 15: PRUEBAS UNITARIAS (src/test/java) ---
    testImplementation("io.kotest:kotest-runner-junit5:5.8.6")
    testImplementation("io.kotest:kotest-assertions-core:5.8.8")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.6")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation(libs.junit)

    // --- DEPENDENCIAS GUÍA 15: PRUEBAS DE UI (src/androidTest/java) ---
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.2")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.2")
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.compose.material3:material3-window-size-class")
}

// Configuración obligatoria para habilitar la plataforma JUnit 5 (Kotest)
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

