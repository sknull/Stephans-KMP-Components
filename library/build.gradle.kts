/**
 * LIBRARY
 */

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    id("com.android.library")
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "de.visualdigits.kmp"
version = "0.9.0-SNAPSHOT"

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
                implementation("org.jetbrains.compose.foundation:foundation:1.10.3")
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

mavenPublishing {
//    publishToMavenCentral()
//    signAllPublications()

    coordinates(group.toString(), "stephans-kmp-components", version.toString())

//    pom {
//        name = "Stephans KMP Components"
//        description = "Some KMP Components."
//        inceptionYear = "2026"
//        url = "https://github.com/sknull/Stephans-KMP-Components/"
//        licenses {
//            license {
//                name = "Apache License, Version 2.0"
//                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
//                distribution = "repo"
//                comments = "A business-friendly OSS license"
//            }
//        }
//        developers {
//            developer {
//                id = "sknull"
//                name = "Stephan Knull"
//                url = "https://github.com/sknull"
//            }
//        }
//        scm {
//            connection = "scm:git@github.com:sknull/sknull/Stephans-KMP-Components.git"
//            developerConnection = "scm:git@github.com:sknull/sknull/Stephans-KMP-Components.git"
//            url = "https://github.com/sknull/Stephans-KMP-Components/"
//        }
//    }

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
