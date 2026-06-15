// Root build file — plugins declared here are available to all subprojects
// but not applied until explicitly requested (apply false).
plugins {
    alias(libs.plugins.kotlin.multiplatform)    apply false
    alias(libs.plugins.compose.multiplatform)   apply false
    alias(libs.plugins.compose.compiler)        apply false
    alias(libs.plugins.android.application)     apply false
}
