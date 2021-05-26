val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val neo4j_driver_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.10"
}

group = "com.github.erguerra"
version = "0.0.1"
application {
    mainClass.set("com.github.erguerra.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.neo4j.driver:neo4j-java-driver:$neo4j_driver_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}