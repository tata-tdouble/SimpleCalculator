import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter")
    //testImplementation("io.mockk:mockk:1.13.4")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    testLogging{ events("passed", "skipped", "failed") }
}


kotlin {
    jvmToolchain(17)
}