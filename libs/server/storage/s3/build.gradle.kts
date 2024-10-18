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
val atriumVersion: String by project
val awsSdkKotlinVersion: String by project
val mockkVersion: String by project
val quarkusAwsS3Version: String by project

dependencies {
  implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))
  implementation("io.quarkus:quarkus-arc")
  implementation("io.quarkus:quarkus-kotlin")
  implementation("aws.sdk.kotlin:s3:$awsSdkKotlinVersion")

  implementation(project(":libs:server:storage:core"))

  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
  testImplementation("ch.tutteli.atrium:atrium-fluent:$atriumVersion")
  testImplementation("io.mockk:mockk:$mockkVersion")
  testImplementation("io.quarkus:quarkus-config-yaml")
  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.quarkus:quarkus-jacoco")
  testImplementation("io.quarkiverse.amazonservices:quarkus-amazon-s3:$quarkusAwsS3Version")
}

group = "org.nxcloudce.server.storage"
version = "0.4.1"

java {
  sourceCompatibility = JavaVersion.toVersion(javaVersion)
  targetCompatibility = JavaVersion.toVersion(javaVersion)
}

tasks.withType<Jar> {
  archiveBaseName = "${project.group}.${project.name}"
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
  annotation("jakarta.enterprise.context.ApplicationScoped")
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
