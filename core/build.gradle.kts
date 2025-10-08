import org.gradle.kotlin.dsl.sourceSets
import kotlin.collections.forEach

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.sqldelight)
}


kotlin {

    androidLibrary {
        compileSdk = 36
        minSdk = 29
        namespace = "com.legarrec.pokedex.core"
    }

    val xcfName = "coreKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.koin.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.sqldelight.android)
                implementation(libs.koin.core)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.sqldelight.native)
            }
        }
    }

}

sqldelight {
    println("SQLDELIGHT - CONFIGURATION")
    val subDirs = project.rootDir
        .walk()
        .filter { it.isDirectory && it.name.startsWith("sqldelight-") && it.path.contains("src/commonMain") }
        .toList()
    databases {
        println("SQLDELIGHT - dossiers trouvés : ${subDirs.map { it.path }}")
        val subDirNames = subDirs.map { it.name.removePrefix("sqldelight-") }.distinct()
        println("SQLDELIGHT - sous-dossiers trouvés : $subDirNames")
        subDirNames.forEach { db ->
            val dbName = db.replaceFirstChar { it.uppercase() } + "Database"
            val dbDirs = subDirs.filter { it.name == "sqldelight-$db" }
            val dbPackageName = dbDirs.firstOrNull()
                ?.walkTopDown()
                ?.filter() { it.isDirectory }
                ?.maxBy { it.path.length }
                ?.path
                ?.split("sqldelight-$db/")?.get(1)
                ?.replace("/", ".")

            println("SQLDELIGHT - Configuration de la base : $dbName (packageName: $dbPackageName, dossiers : ${dbDirs})")
            create(dbName) {
                packageName.set(dbPackageName)
                srcDirs.setFrom(dbDirs)
            }
        }
    }
}