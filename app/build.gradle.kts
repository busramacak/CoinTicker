plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id ("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}

android {
    namespace = "com.bmprj.cointicker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bmprj.cointicker"
        minSdk = 24
        targetSdk = 34
        versionCode = 4
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            buildConfigField("String","BASE_URL","\"https://api.coingecko.com/api/v3/\"")
            buildConfigField("String","COINGECKO_URL","\"https://www.coingecko.com/tr/api\"")
            buildConfigField("String","PRIVACY_POLICY_URL","\"https://www.freeprivacypolicy.com/live/621a0752-3204-4349-a92c-01b1055ba261\"")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }

        debug {
            buildConfigField("String","COINGECKO_URL","\"https://www.coingecko.com/tr/api\"")
            buildConfigField("String","BASE_URL","\"https://api.coingecko.com/api/v3/\"")
            buildConfigField("String","PRIVACY_POLICY_URL","\"https://www.freeprivacypolicy.com/live/621a0752-3204-4349-a92c-01b1055ba261\"")

        }


        buildFeatures{
            //noinspection DataBindingWithoutKapt
            dataBinding = true
            buildConfig = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    implementation("com.google.firebase:firebase-auth-ktx")
    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))

    implementation ("com.google.dagger:hilt-android:2.47")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.1")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    kapt ("androidx.room:room-compiler:2.6.0")
    kapt ("com.google.dagger:hilt-android-compiler:2.47")
    kapt ("androidx.hilt:hilt-compiler:1.1.0")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.9.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}