plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.scesi.appmobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.scesi.appmobile"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.1")

//    implementation ("com.github.antonKozyriatskyi:CircularProgressIndicator:1.3.2")

    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")
    implementation ("com.squareup.okhttp3:okhttp:4.9.2")

// Coroutine support
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
// Glide for image loading
   // implementation ("com.github.bumptech.glide:glide:4.13.2")
   // kapt ("com.github.bumptech.glide:compiler:4.13.2")

// Glide
//    implementation 'com.github.bumptech.glide:glide:4.15.1'

    // CircularProgressIndicator
//    implementation 'com.github.antonKozyriatskyi:CircularProgressIndicator:1.3.0'

//    // Coroutines for asynchronous operations
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Activity
    // implementation("androidx.activity:activity-ktx:1.2.2")


}