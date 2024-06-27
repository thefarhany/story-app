plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id ("kotlin-parcelize")
    id("com.google.devtools.ksp")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

android {
    namespace = "com.example.storyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.storyapp"
        minSdk = 24
        targetSdk = 33
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation (libs.material.v110alpha09)

    implementation (libs.androidx.datastore.preferences)
    implementation (libs.androidx.datastore.core)

    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.paging.runtime.ktx.v310)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)
    implementation (libs.glide)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.exifinterface)
    implementation(libs.androidx.paging.runtime.ktx)

    implementation(libs.androidx.core.testing)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.mockito.inline)
    implementation(libs.mockito.core)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.androidx.espresso.idling.resource)
    implementation(libs.androidx.espresso.intents)
    implementation(libs.androidx.ui.test.android)

    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.paging.runtime.ktx.v310)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.core.testing.v210)
    testImplementation(libs.kotlinx.coroutines.test.v161)
    androidTestUtil(libs.androidx.orchestrator)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.runner)
}