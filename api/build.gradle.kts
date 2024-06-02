plugins {
    id("java")
}

group = "com.github.devcyntrix"
version = "3.0.0"

java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    compileOnly("org.projectlombok:lombok:1.18.32")

    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")

    annotationProcessor("org.projectlombok:lombok:1.18.32")
}
