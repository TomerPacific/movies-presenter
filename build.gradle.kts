buildscript {
    extra.set("compose_version", "1.4.0")

    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.0")
    }
}

plugins {
    id("com.android.application") version "8.9.1" apply false
    id("com.android.library") version "8.9.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}