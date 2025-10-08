pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") // JetBrains Kotlin repository
    }
}

rootProject.name = "Pokedex"
include(":common")
include(":ios")
include(":ios:Pokedex")
include(":app")
project(":app").projectDir = File(rootDir, "android/app")

include(":core")
