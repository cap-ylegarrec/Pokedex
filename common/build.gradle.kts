import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.native.coroutine)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
}

android {
    compileSdk = 36
    defaultConfig {
        minSdk = 29
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    namespace = "com.legarrec.pokedex.common"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    tasks.register("assembleReleaseXCFramework") {
        dependsOn(
            "linkReleaseFrameworkIosArm64",
            "linkReleaseFrameworkIosSimulatorArm64"
        )
        doLast {
            val xcodeDir = buildDir.resolve("XCFrameworks/release")
            xcodeDir.mkdirs()
            exec {
                commandLine(
                    "xcodebuild",
                    "-create-xcframework",
                    "-framework",
                    "${buildDir}/bin/iosArm64/releaseFramework/shared.framework",
                    "-framework",
                    "${buildDir}/bin/iosSimulatorArm64/releaseFramework/shared.framework",
                    "-output",
                    "${xcodeDir}/shared.xcframework"
                )
            }
        }
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }
        commonMain.dependencies {
            api(project(":core"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.coroutines.extensions)
            implementation(libs.primitive.adapters)
        }
        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
            implementation(libs.sqldelight.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.coroutine.test)
        }
    }
}

//sqldelight {
//    databases {
//        create("PokemonDB") {
//            packageName.set("pokemon.data.datasource")
//        }
//    }
//}

repositories {
    mavenCentral()
    google()
}
