pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.dropbox.focus") version "0.4.0"
}

configure<com.dropbox.focus.FocusExtension> {
    allSettingsFileName.set("includes.gradle.kts")
    focusFileName.set(".focus")
}
