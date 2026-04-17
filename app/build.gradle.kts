plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.gsm.newscircle"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.gsm.newscircle"
        minSdk = 24
        targetSdk = 37
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
            buildConfigField("String", "API_KEY", "\"826fd61f08c74ae3a856799fc4eb9a35\"")
        }
        debug {
            isDebuggable = true
            buildConfigField("String", "API_KEY", "\"826fd61f08c74ae3a856799fc4eb9a35\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Dagger2
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Core OkHttp library
    implementation(libs.okhttp)

    // Logging Interceptor (separate dependency)
    implementation(libs.logging.interceptor)

    // Custom Tab
    implementation(libs.androidx.browser)

    // Glide
    implementation(libs.glide)

    // Lottie
    implementation(libs.lottie)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // ViewModelScope and LifeCycleScope
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}