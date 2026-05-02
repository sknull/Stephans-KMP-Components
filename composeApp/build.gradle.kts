/**
 * DEMO APP
 */

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    id("com.google.devtools.ksp") version "2.3.6"
}

version = "1.0.0"

kotlin {
    jvm()
    jvmToolchain(21)
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }

        val androidMain by getting {
            dependencies {
                // android
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)

                // android tv
                implementation(project.dependencies.platform("androidx.compose:compose-bom:2026.03.00"))
                implementation(libs.androidx.tv.material)
                implementation(libs.androidx.ui.tooling)
                implementation(libs.androidx.ui.tooling.preview)
            }
        }

        commonMain.dependencies {
            implementation(compose.components.resources)

            implementation(libs.bundles.compose)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.jetbrains.compose.navigation)

            implementation(libs.kotlin.xml.util)
            implementation(libs.kotlin.xml.serialization)
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.io.core)

            implementation(libs.kermit)

            implementation(project(":library"))
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.junit.jupiter.api)
            implementation(libs.junit.jupiter.engine)
            implementation(libs.junit.platform.launcher)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.skiko.awt.runtime.windows.x64)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.kotlinx.io.core.jvm)
        }

        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.junit.jupiter.api)
            implementation(libs.junit.jupiter.engine)
            implementation(libs.junit.platform.launcher)
        }
    }
}

configurations.all {
    exclude(group = "ch.qos.logback", module = "logback-classic")
    exclude(group = "ch.qos.logback", module = "logback-core")
}

base {
    archivesName.set("StephansComponents")
}

android {
    namespace = "de.visualdigits.common"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "de.visualdigits.common"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/io.netty.versions.properties"

            // Schließt ALLE plattformspezifischen Metadaten aus (Native, JS, Wasm)
            excludes += "**/default/linkdata/**"
            excludes += "**/default/manifest"
            excludes += "**/default/module"
//            excludes += "**/*.knm"
//            excludes += "**/*.kotlin_metadata"

            // Speziell für deinen neuen Fehler (JS/Wasm Pfade)
            excludes += "jsAndWasmJsMain/**"
            excludes += "wasmJsMain/**"
            excludes += "jsMain/**"

            pickFirsts.add("META-INF/kotlin-project-structure-metadata.json")
            pickFirsts.add("META-INF/kotlinx-serialization-json.kotlin_module")
            pickFirsts.add("META-INF/resource_loader.kotlin_module")
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

configurations.all {
    exclude(group = "org.jetbrains.compose.material", module = "material-desktop")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Tar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

compose.desktop {
    application {
        mainClass = "de.visualdigits.common.MainKt"

        nativeDistributions {
            packageName = "de.visualdigits.common"
            packageVersion = project.version.toString()
            includeAllModules = false
            modules(
                "java.instrument",
                "jdk.unsupported",
                "java.desktop",
                "java.xml",
                "java.naming",
                "java.prefs",
                "java.sql",
                "java.net.http"
            )
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            windows {
//                iconFile.set(project.file("src/commonMain/composeResources/drawable/Msfs2024Tools.ico"))
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "de.visualdigits.compose.resources"
}
