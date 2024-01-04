plugins {
  kotlin("jvm")
  kotlin("plugin.allopen")
  kotlin("plugin.noarg")
  id("io.quarkus")
  id("com.diffplug.spotless")
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
  implementation("io.quarkus:quarkus-smallrye-openapi")

  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
  testImplementation("io.mockk:mockk:$mockkVersion")
  testImplementation("io.quarkiverse.mockk:quarkus-junit5-mockk:$quarkusMockkVersion")
  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.quarkus:quarkus-test-hibernate-reactive-panache")
  testImplementation("io.rest-assured:rest-assured")
}

group = "org.nxcloudce"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.toVersion(javaVersion)
  targetCompatibility = JavaVersion.toVersion(javaVersion)
}

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

allOpen {
  annotation("jakarta.ws.rs.Path")
  annotation("jakarta.enterprise.context.ApplicationScoped")
  annotation("jakarta.persistence.Entity")
  annotation("io.quarkus.test.junit.QuarkusTest")
}

noArg {
  annotation("io.quarkus.mongodb.panache.common.MongoEntity")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions.jvmTarget = javaVersion
  kotlinOptions.javaParameters = true
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
