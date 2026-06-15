@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

import java.util.Properties

// ── Android SDK detection ────────────────────────────────────────────────────
// Android is opt-in because it requires the Android SDK to be installed.
// Set  kmp.android.enabled=true  in gradle.properties and add  sdk.dir=...
// to local.properties to enable the Android target.
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.reader().use { load(it) }
}
val androidSdkDir: String? =
    localProps.getProperty("sdk.dir")
        ?: System.getenv("ANDROID_HOME")
        ?: System.getenv("ANDROID_SDK_ROOT")

val androidEnabled: Boolean =
    (project.findProperty("kmp.android.enabled")?.toString()?.toBoolean() ?: false)
        && androidSdkDir != null

// ── Plugins ──────────────────────────────────────────────────────────────────
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    // android.application is declared in root with apply false;
    // it's applied conditionally below to avoid SDK-not-found errors.
}

if (androidEnabled) {
    apply(plugin = "com.android.application")
}

// ── Kotlin Multiplatform targets ─────────────────────────────────────────────
kotlin {
    // Automatically creates iosMain, appleMain, nativeMain shared source sets
    applyDefaultHierarchyTemplate()

    // ── Targets ──────────────────────────────────────────────────────────────
    if (androidEnabled) {
        androidTarget()
    }

    // JVM target named "desktop" → source set: desktopMain
    jvm("desktop")

    // Kotlin/Wasm → compiles to WebAssembly, runs in a browser
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    // iOS targets (build requires macOS + Xcode; code is here for reference)
    // Note: iosX64 (Intel Mac simulator) was removed in CMP 1.11.0 — ARM64 only.
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // ── Source sets ──────────────────────────────────────────────────────────
    sourceSets {
        // Shared across ALL platforms
        @Suppress("DEPRECATION")  // compose.xxx shorthand — deprecated in CMP 1.11.0 but still correct
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }

        // Android-only (enabled conditionally above)
        @Suppress("DEPRECATION")
        if (androidEnabled) {
            androidMain.dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
            }
        }

        // Desktop (JVM)
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        // Web (Kotlin/Wasm) — no extra dependencies needed beyond commonMain
    }
}

// ── Android application config ────────────────────────────────────────────────
// Configured at runtime so the type-safe `android {}` DSL is accessed via
// extensions.configure<> to avoid Kotlin DSL compilation errors when AGP is
// applied dynamically (not via the plugins {} block).
if (androidEnabled) {
    @Suppress("UNCHECKED_CAST")
    (extensions.findByName("android") as? com.android.build.gradle.AppExtension)?.apply {
        namespace = "com.shnaki.kmpexample"
        compileSdkVersion(libs.versions.android.compileSdk.get().toInt())
        defaultConfig {
            applicationId = "com.shnaki.kmpexample"
            minSdkVersion(libs.versions.android.minSdk.get().toInt())
            targetSdkVersion(libs.versions.android.targetSdk.get().toInt())
            versionCode = 1
            versionName = "1.0"
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        packagingOptions {
            resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        buildTypes {
            getByName("release") { isMinifyEnabled = false }
        }
    }
}

// ── Desktop application entry point ──────────────────────────────────────────
compose.desktop {
    application {
        mainClass = "com.shnaki.kmpexample.MainKt"
    }
}
