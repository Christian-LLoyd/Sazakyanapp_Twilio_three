// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath ("androidx.navigation:navigation-safe-args-generator:2.7.4")
        classpath ("com.android.tools.build:gradle:8.7.2")
        classpath ("com.google.gms:google-services:4.4.2")
    }

    repositories {
        maven ( "https://jitpack.io")
    }
}


plugins {
    id("com.android.application") version "8.3.2" apply false
    id ("org.jetbrains.kotlin.android") version "1.9.22" apply false // Use latest stable version
    id("androidx.navigation.safeargs") version "2.4.2" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.android.library") version "8.7.2" apply false

}