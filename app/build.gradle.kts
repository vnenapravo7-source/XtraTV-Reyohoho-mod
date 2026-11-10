plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.apollo)
}

kotlin {
    jvmToolchain(21)
}

val supportedTvAbis = setOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
val releaseKeyPath = providers.environmentVariable("RELEASE_KEYSTORE_PATH")
val targetTvAbi = providers.gradleProperty("targetAbi").orElse("armeabi-v7a").get()
require(targetTvAbi in supportedTvAbis) { "Unsupported targetAbi: $targetTvAbi" }

android {
    signingConfigs {
        create("modRelease") {
            if (releaseKeyPath.isPresent) {
                storeFile = file(releaseKeyPath.get())
                storePassword = providers.environmentVariable("RELEASE_KEYSTORE_PASSWORD").get()
                keyAlias = providers.environmentVariable("RELEASE_KEY_ALIAS").get()
                keyPassword = providers.environmentVariable("RELEASE_KEY_PASSWORD").get()
            }
        }
        getByName("debug") {
            keyAlias = "debug"
            keyPassword = "123456"
            storeFile = file("debug-keystore.jks")
            storePassword = "123456"
        }
    }
    namespace = "com.github.andreyasadchy.xtra"
    compileSdk = 37

    defaultConfig {
        // own package: installs next to the original Xtra
        applicationId = "com.github.razenfr.xtratv.reyohoho"
        minSdk = 24
        targetSdk = 37
        versionCode = 147
        versionName = "1.2.13-reyohoho.6-lite"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = if (releaseKeyPath.isPresent) signingConfigs.getByName("modRelease") else null
            ndk { abiFilters += targetTvAbi }
        }
        create("optimized") {
            initWith(getByName("release"))
            // Update the installed test application in place, preserving accounts and preferences.
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-TV"
            isDebuggable = false
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
        viewBinding = true
    }
    androidResources {
        generateLocaleConfig = true
    }
    lint {
        disable += "ContentDescription"
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    compileOnly("com.google.j2objc:j2objc-annotations:3.0.0") // OkHttpDataSource SettableFuture

    implementation(libs.material)
    implementation(libs.markwon.core)
    implementation(libs.markwon.linkify)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.activity.compose)
    implementation(libs.tv.material)

    implementation(libs.activity)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.coordinatorlayout)
    implementation(libs.core)
    implementation(libs.fragment)
    implementation(libs.lifecycle.service)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.paging.runtime)
    implementation(libs.preference.ktx)
    implementation(libs.recyclerview)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.paging)
    implementation(libs.swiperefreshlayout)
    implementation(libs.viewpager2)
    implementation(libs.webkit)
    implementation(libs.work.runtime)

    implementation(libs.cronet.api)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.conscrypt)
    implementation(libs.serialization.json)
    implementation(libs.apollo.api)

    implementation(libs.media3.exoplayer)
    implementation(libs.media3.exoplayer.hls)
    implementation(libs.media3.ui)
    implementation(libs.media3.session)

    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.okhttp)

    implementation(libs.glide)
    ksp(libs.glide.ksp)
    implementation(libs.glide.okhttp)
    implementation(libs.glide.webpdecoder)

    implementation(libs.coroutines)
}

apollo {
    @Suppress("ApolloEndpointNotConfigured")
    service("service") {
        packageName.set("com.github.andreyasadchy.xtra.graphql")
    }
}

// Delete large build log files from ~/.gradle/daemon/X.X/daemon-XXX.out.log
// Source: https://discuss.gradle.org/t/gradle-daemon-produces-a-lot-of-logs/9905
File("${project.gradle.gradleUserHomeDir.absolutePath}/daemon/${project.gradle.gradleVersion}").listFiles()?.forEach {
    if (it.name.endsWith(".out.log")) {
        // println("Deleting gradle log file: $it") // Optional debug output
        it.delete()
    }
}
