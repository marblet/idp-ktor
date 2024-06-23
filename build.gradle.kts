
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project
val detekt_version: String by project
val kotest_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

group = "com.marblet"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-gson-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.arrow-kt:arrow-core:1.2.4")

    // database
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("mysql:mysql-connector-java:8.0.33")

    // testing
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // kotest
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.kotest:kotest-framework-datatest:$kotest_version")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.4.0")

    // detekt
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detekt_version")
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
