pluginManagement {
  val quarkusPluginVersion: String by settings
  val quarkusPluginId: String by settings
  val kotlinVersion: String by settings
  val jnxplusGradlePluginVersion: String by settings
  val spotlessVersion: String by settings
  repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }
  plugins {
    id(quarkusPluginId) version quarkusPluginVersion
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("io.github.khalilou88.jnxplus") version jnxplusGradlePluginVersion
    id("com.diffplug.spotless") version spotlessVersion
  }
}

rootProject.name = "nx-cloud-ce"
include("apps:api")
