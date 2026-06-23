/**
 * LIBRARY
 */

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    id("com.android.library")
    `maven-publish`
}

group = "de.visualdigits.kmp"
version = "0.0.1-SNAPSHOT"

kotlin {
    androidTarget {
        withSourcesJar()
        publishLibraryVariants("release", "debug")

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    jvm("desktop") {
        withSourcesJar()
    }

    jvmToolchain(21)

    sourceSets {
        val androidMain by getting {
            dependencies {
                // android
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.appcompat)

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
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kermit)
            implementation(libs.compose.colorpicker)

            // needed in client projects
            api(libs.kotlinx.io.core)
            api(libs.kotlinx.datetime)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.junit.jupiter.api)
            implementation(libs.junit.jupiter.engine)
            implementation(libs.junit.platform.launcher)
        }

        val jvmMain by creating {
            dependsOn(commonMain.get())
        }
        val desktopMain by getting {
            dependsOn(jvmMain)

            dependencies {
                implementation(libs.compose.foundation)

                // needed in client projects
                api(libs.kotlinx.io.core.jvm)
            }
        }

        val jvmTest by creating {
            dependsOn(commonTest.get())
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.junit.jupiter.api)
                implementation(libs.junit.jupiter.engine)
                implementation(libs.junit.platform.launcher)
            }
        }

        val desktopTest by getting {
            dependsOn(jvmTest)
        }
    }
}

android {
    namespace = "de.visualdigits.kmp.components"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    // Das ersetzt withJava() im klassischen Plugin
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

publishing {
    publications {
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sknull/Stephans-KMP-Components")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
