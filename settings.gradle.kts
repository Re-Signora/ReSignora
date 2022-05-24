pluginManagement {
    resolutionStrategy {
        eachPlugin {
            // if (requested.id.id == "com.android.library") {
            //     useModule("com.android.tools.build:gradle:${requested.version}")
            // }
            // if (requested.id.id == "com.android.application") {
            //     useModule("com.android.tools.build:gradle:${requested.version}")
            // }
        }
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "re-signora"
include(
    "core",
    "pc",
    "app"
)
