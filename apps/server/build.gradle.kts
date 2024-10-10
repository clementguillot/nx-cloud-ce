import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  kotlin("jvm")
  kotlin("plugin.allopen")
  id("io.quarkus")
  id("com.diffplug.spotless")
  id("jacoco")
}

repositories {
  mavenCentral()
  mavenLocal()
}

val javaVersion: String by project
val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val ktlintVersion: String by project
val mockkVersion: String by project
val quarkusMockkVersion: String by project
val atriumVersion: String by project
val azureBlobStorageVersion: String by project
val azureIdentityVersion: String by project
val cliktVersion: String by project
val jacksonDatatypeJsr310Version: String by project

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))
  implementation("io.quarkus:quarkus-arc")
  implementation("io.quarkus:quarkus-config-yaml")
  implementation("io.quarkus:quarkus-kotlin")
  implementation("io.quarkus:quarkus-mongodb-panache-kotlin")
  implementation("io.quarkus:quarkus-resteasy-reactive")
  implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
  implementation("io.quarkus:quarkus-security")
  implementation("io.quarkus:quarkus-smallrye-context-propagation")
  implementation("io.quarkus:quarkus-smallrye-openapi")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonDatatypeJsr310Version")
  implementation("com.github.ajalt.clikt:clikt:$cliktVersion")
  implementation("com.azure:azure-storage-blob:$azureBlobStorageVersion")
  implementation("com.azure:azure-identity:$azureIdentityVersion")
  implementation("com.azure:azure-sdk-bom:1.2.28")
  implementation("com.microsoft.azure:msal4j:1.16.2")
  implementation("io.projectreactor.netty:reactor-netty-http:1.0.45")
  implementation("com.fasterxml.woodstox:woodstox-core:7.0.0")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonDatatypeJsr310Version")
//  implementation("com.azure:azure-aot-graalvm-support:1.0.0-beta.3")
//  implementation("com.azure:azure-aot-graalvm-support-netty:1.0.0-beta.3")

  implementation(project(":libs:server:domain"))
  implementation(project(":libs:server:persistence"))
  implementation(project(":libs:server:storage:azure"))
  implementation(project(":libs:server:storage:core"))
  implementation(project(":libs:server:storage:gcs"))
  implementation(project(":libs:server:storage:s3"))

  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
  testImplementation("ch.tutteli.atrium:atrium-fluent:$atriumVersion")
  testImplementation("io.mockk:mockk:$mockkVersion")
  testImplementation("io.quarkiverse.mockk:quarkus-junit5-mockk:$quarkusMockkVersion")
  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.quarkus:quarkus-junit5-internal")
  testImplementation("io.quarkus:quarkus-jacoco")
  testImplementation("io.quarkus:quarkus-test-hibernate-reactive-panache")
  testImplementation("io.rest-assured:rest-assured")
}

group = "org.nxcloudce.server"
version = "0.4.0"

java {
  sourceCompatibility = JavaVersion.toVersion(javaVersion)
  targetCompatibility = JavaVersion.toVersion(javaVersion)
}

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
  finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
  reports {
    csv.required.set(false)
    xml.required.set(true)
    xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/jacoco.xml"))
  }
}

allOpen {
  annotation("jakarta.ws.rs.Path")
  annotation("jakarta.enterprise.context.ApplicationScoped")
  annotation("jakarta.persistence.Entity")
  annotation("io.quarkus.test.junit.QuarkusTest")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-Xjsr305=strict")
    jvmTarget = JvmTarget.fromTarget(javaVersion)
    javaParameters = true
  }
}

spotless {
  kotlin {
    ktlint(ktlintVersion)
  }

  kotlinGradle {
    target("*.gradle.kts")
    ktlint(ktlintVersion)
  }
}
