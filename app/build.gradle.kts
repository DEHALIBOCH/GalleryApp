plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    namespace = "kz.project.gallery"
    compileSdk = 33

    defaultConfig {
        applicationId = "kz.project.gallery"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        viewBinding = true
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // KTX
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.activity:activity-ktx:1.7.2")

    // Dagger2
    implementation("com.google.dagger:dagger:2.44.2")
    kapt("com.google.dagger:dagger-compiler:2.44.2")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    //noinspection KaptUsageInsteadOfKsp
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava3:$retrofitVersion")

    // RxJava
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("com.jakewharton.rxbinding4:rxbinding:4.0.0")

    // SwipeRefresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

    // ViewBinding delegation
    implementation("com.github.yogacp:android-viewbinding:1.0.4")

    // Pagination
    implementation("androidx.paging:paging-runtime-ktx:3.2.0")
    implementation("androidx.paging:paging-rxjava3:3.2.0")
}