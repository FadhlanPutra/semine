plugins {
    // AGP sudah ada di classpath root (lewat android-application), jadi id dipakai tanpa versi.
    id("com.android.library")
}

android {
    namespace = "com.lann.semine.core"
    compileSdk = 37

    defaultConfig {
        minSdk = 29
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
