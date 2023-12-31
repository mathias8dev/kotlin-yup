plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.diffplug.spotless") version "6.23.3"
}


spotless {
    kotlin {
        target("**/*.kt")
        ktlint("1.1.0")
    }
}