plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mountreachsolution.kisaan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mountreachsolution.kisaan"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.loopj.android:android-async-http:1.4.11")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.google.zxing:core:3.4.1")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0") // QR scanner
    implementation ("com.google.android.gms:play-services-location:21.2.0")
    implementation ("androidx.biometric:biometric:1.2.0-alpha05")
    // ViewPager2
    implementation ("androidx.viewpager2:viewpager2:1.1.0-alpha01")

    // Material for TabLayout indicators
    implementation ("com.google.android.material:material:1.10.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //gson convertor
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("org.tensorflow:tensorflow-lite:2.13.0")
    implementation ("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation ("org.tensorflow:tensorflow-lite-gpu:2.13.0")

    //gif
    implementation("com.github.bumptech.glide:glide:4.16.0")
}