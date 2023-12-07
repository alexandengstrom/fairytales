import java.util.Properties

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
val API_KEY: String = gradleLocalProperties(rootDir).getProperty("API_KEY")
val MAPS_KEY: String = gradleLocalProperties(rootDir).getProperty("MAPS_KEY")


plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.app"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_KEY", API_KEY)
            resValue("string", "MAPS_KEY", MAPS_KEY)
        }

        debug {
            buildConfigField("String", "API_KEY", API_KEY)
            resValue("string", "MAPS_KEY", MAPS_KEY)
        }

        defaultConfig {
            buildConfigField("String", "API_KEY", API_KEY)
            resValue("string", "MAPS_KEY", MAPS_KEY)
        }


    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.1.2")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.android.gms:play-services-location:17.0.0")
    implementation("com.google.firebase:firebase-analytics:21.5.0")
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

