import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    testImplementation("com.github.tomakehurst:wiremock-jre8:2.35.0")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}
