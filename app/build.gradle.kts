plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.quangtrader.cryptoportfoliotracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.quangtrader.cryptoportfoliotracker"
        minSdk = 29
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
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)



    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //Timber
    implementation(libs.timber)
    //Lottie
    implementation(libs.lottie)
    //Glide
    implementation(libs.glide)



    //ssd, ssp
    implementation(libs.sdp)
    implementation(libs.ssp)
    //coroutines
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.extensions)

    implementation(libs.rx.pref)

    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.process)
    //Circle ImageView
    implementation(libs.circle.image)
    implementation(libs.bundles.uber.autodispose.libs)
    //Firebase
    implementation(libs.bundles.firebase.libs)

    implementation(libs.gdpr)


    // RxJava
    implementation(libs.rxandroid)
    implementation(libs.rxjava)
    implementation(libs.rxlifecycle)
    implementation(libs.rxlifecycle.kotlin)
    implementation(libs.rxlifecycle.android)
    implementation(libs.rxlifecycle.android.lifecycle)

    implementation(libs.zip4j)
    implementation(libs.kotlinx.serialization.json.v132)
    implementation(libs.gson)
    implementation(libs.jetbrains.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.protobuf)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v283)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.adapter.rxjava2)
    //Billing
    implementation(libs.billing.ktx)
    implementation (libs.stetho.okhttp3)
    implementation (libs.okhttp3.okhttp)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.okhttp3.okhttp)
    implementation (libs.flaviofaria.kenburnsview)



    implementation (libs.makeramen.roundedimageview)
    implementation(libs.commons.codec)
    implementation(libs.androidx.runtime)
    implementation (libs.carouselrecyclerview)



    implementation(libs.dots.indicator)


    implementation("nl.joery.animatedbottombar:library:1.1.0")









    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}