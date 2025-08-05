buildscript {
    extra.set("compose_version", "1.4.0")
}

plugins {
    alias(libs.plugins.android.app)     apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android)  apply false
    alias(libs.plugins.compose.compiler) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}