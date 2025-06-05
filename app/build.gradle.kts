plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.rankinguni"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.rankinguni"
        minSdk = 26
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
    // Enable ViewBinding
    buildFeatures {
        viewBinding = true
    }
    // Specify aaptOptions to prevent issues with tflite models
    aaptOptions {
        noCompress += ".tflite"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // WebView
    implementation("androidx.webkit:webkit:1.10.0")

    // TensorFlow Lite for ML model integration
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.4") // Or other task library if needed
    implementation("org.tensorflow:tensorflow-lite-gpu-delegate-plugin:0.4.4") // Optional: for GPU acceleration
    implementation("org.tensorflow:tensorflow-lite:2.15.0") // Base TFLite library

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // annotationProcessor("com.github.bumptech.glide:compiler:4.12.0") // For Java projects, not usually needed for Kotlin with KAPT

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}