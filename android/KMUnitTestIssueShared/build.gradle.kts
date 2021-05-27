import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    // This plugin is needed since this is an Android library (not an application).
    id("com.android.library")

    kotlin("multiplatform")
}

// This is needed in order to utilize Android APIs from this framework.
android {
    defaultConfig {
        minSdkVersion(23)
        compileSdkVersion(28)
    }
}

// 'kotlin' syntax is dependent on org.jetbrains.kotlin:kotlin-gradle-plugin
kotlin {
    android()

    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "KMUnitTestIssueShared"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
            }
        }
    }

    // Supposedly adds necessary dependencies for all test targets - Should allow iosTest and androidTest to access these, but they don't
    sourceSets {
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }

    sourceSets["androidMain"].dependencies {
    }

    // Supposedly adds necessary dependencies for all test targets - doesn't seem to be working
    sourceSets["androidTest"].dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-annotations-common"))
    }

    // Supposedly adds necessary dependencies for all test targets - doesn't seem to be needed
    sourceSets["iosTest"].dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-annotations-common"))
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"

    //selecting the right configuration for the iOS framework depending on the Xcode environment variables
    var configuration = System.getenv("CONFIGURATION") ?: "DEBUG"
    project.logger.warn("[build.gradle.kts] Xcode CONFIGURATION: $configuration")

    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(configuration)
    inputs.property("mode", configuration)

    dependsOn(framework.linkTask)

    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)

    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText("#!/bin/bash\nexport 'JAVA_HOME=${System.getProperty("java.home")}'\ncd '${rootProject.rootDir}'\n./gradlew \$@\n")
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)
dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.testng:testng:6.9.6")
}
