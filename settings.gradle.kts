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
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Cambiar a PREFER_SETTINGS
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io") // Agrega esta línea
    }
}

rootProject.name = "AppMobile"
include(":app")
