plugins {
  kotlin("jvm")
  kotlin("plugin.allopen")
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

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
  implementation("io.quarkus:quarkus-arc")
  implementation("io.quarkus:quarkus-config-yaml")
  implementation("io.quarkus:quarkus-kotlin")
  implementation("io.quarkus:quarkus-resteasy-reactive")
  implementation("io.quarkus:quarkus-smallrye-openapi")

  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.rest-assured:rest-assured")
}

group = "com.nxcloudce"
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
  annotation("io.quarkus.test.junit.QuarkusTest")
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
