
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
    id("kotlinx-serialization")
    kotlin("plugin.serialization")
}

android {

    namespace = BuildConfig.applicationId
    compileSdk = BuildConfig.compileSdk
    defaultConfig {
        applicationId = BuildConfig.applicationId
        minSdk = BuildConfig.minSdkVersion
        targetSdk = BuildConfig.targetSdkVersion
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName
        testInstrumentationRunner = BuildConfig.testInstrumentationRunner

        ndk {
            //不配置则默认构建并打包所有可用的ABI
            abiFilters.addAll(arrayListOf("x86_64", "armeabi-v7a", "arm64-v8a"))
        }
// 开启 Dex 分包
        multiDexEnabled = true
        vectorDrawables {
            useSupportLibrary = true
        }
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.coresplashscreen)

    implementation(AndroidX.Lifecycle.runtimeKtx)
    implementation(AndroidX.Compose.activity)
    implementation(AndroidX.Compose.ui)
    implementation(AndroidX.Compose.tooling_preview)
    implementation(AndroidX.Compose.material3)
    implementation(AndroidX.multidex)
    implementation(AndroidX.Compose.accompanist.insets)
    implementation(AndroidX.Compose.accompanist.placeholder)
    implementation(AndroidX.Compose.accompanist.systemuicontroller)

    implementation(AndroidX.Navigation.compose)
    implementation(AndroidX.Navigation.uiKtx)
    implementation(AndroidX.Navigation.animation)
    implementation (AndroidX.Documentfile)
    //Hilt
    implementation(AndroidX.Hilt.common)
    kapt(AndroidX.Hilt.android_compiler)
    implementation(AndroidX.Hilt.navigation_compose)

    //Kotlin
    implementation (Kotlin.serialization)
    implementation (Kotlin.parcelize_runtime)

    // OkHttp
    implementation(ThirdPart.OkHttp.okhttp)
    implementation(ThirdPart.OkHttp.loggingInterceptor)

    //DataStore
    implementation(AndroidX.DataStore.preferences)
    implementation(AndroidX.DataStore.core)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}