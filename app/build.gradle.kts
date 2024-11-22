plugins {
    alias(libs.plugins.android.application)
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.learning.movietracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.learning.movietracker"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    // gson - JSON to POJO
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // retrofit - API call
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Glide - Image display in android view
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    //hilt dagger
    val hilt_version = "2.51.1"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    annotationProcessor("com.google.dagger:hilt-android-compiler:$hilt_version")

    val lifecycle_version = "2.8.6"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata:$lifecycle_version")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")
    //print calling API URI in logs
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    //color palette - to find dominant color in image
    implementation("androidx.palette:palette:1.0.0")
    //color palatte - to find image from url
    implementation("com.squareup.picasso:picasso:2.71828")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}