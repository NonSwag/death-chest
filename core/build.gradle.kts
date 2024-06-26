import io.papermc.hangarpublishplugin.model.Platforms

plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
}

group = project(":api").group
group = project(":api").version

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://maven.enginehub.org/repo/")

    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://raw.githubusercontent.com/FabioZumbi12/RedProtect/mvn-repo/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    maven("https://repository.minecodes.pl/releases")
    maven("https://repo.thenextlvl.net/releases")

    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.google.inject:guice:7.0.0")
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")

    compileOnly("net.kyori:adventure-text-minimessage:4.17.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.17.0")

    // Command library
    implementation("cloud.commandframework:cloud-core:1.7.1")
    implementation("cloud.commandframework:cloud-bukkit:1.7.1")

    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("org.bstats:bstats-bukkit:3.0.2")

    implementation(project(":api"))
    runtimeOnly(project(":paper"))
    runtimeOnly(project(":spigot"))

    testRuntimeOnly(project(":paper"))
    testRuntimeOnly(project(":spigot"))

    // Protection Support
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.7")
    compileOnly("com.plotsquared:PlotSquared-Core:6.8.1") { isTransitive = false }
    compileOnly("com.plotsquared:PlotSquared-Bukkit:6.8.1") { isTransitive = false }
    compileOnly("com.github.TechFortress:GriefPrevention:16.18") { isTransitive = false }
    compileOnly("br.net.fabiozumbi12.RedProtect:RedProtect-Core:7.7.3") { isTransitive = false }
    compileOnly("br.net.fabiozumbi12.RedProtect:RedProtect-Spigot:7.7.3") { isTransitive = false }
    compileOnly("pl.minecodes.plots:plugin-api:4.0.0")
    compileOnly("net.thenextlvl.protect:api:2.0.6")

    // Animation Support
    compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0") { isTransitive = false }

    // Placeholder API
    compileOnly("me.clip:placeholderapi:2.11.2") { isTransitive = false }

    // Lock
    compileOnly("com.griefcraft:lwc:2.3.2-dev")

    compileOnly("org.jetbrains:annotations:24.1.0")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.shadowJar {
    relocate("org.bstats", "com.github.devcyntrix.deathchest.metrics")
    archiveBaseName.set("deathchest")
}

tasks.runServer {
    dependsOn("shadowJar")
    minecraftVersion("1.20.6")
}

hangarPublish {
    publications.register("DeathChest") {
        version.set(project.version as String)
        changelog.set("https://github.com/DevCyntrix/death-chest/blob/main/CHANGELOG")

        apiKey.set(System.getenv("API_KEY"))

        channel.set(if (project.version.toString().contains('-')) "Snapshot" else "Release")

        platforms.register(Platforms.PAPER) {
            jar = tasks.shadowJar.flatMap { it.archiveFile }
            println(jar.get().asFile)
            println(version)
            platformVersions.set(listOf("1.17-1.20.6"))
            dependencies.url("ProtocolLib", "https://www.spigotmc.org/resources/protocollib.1997/") {
                required.set(false)
            }
            dependencies.url("PlaceholderAPI", "https://www.spigotmc.org/resources/placeholderapi.6245/") {
                required.set(false)
            }
            dependencies.url("GriefPrevention", "https://www.spigotmc.org/resources/griefprevention.1884/") {
                required.set(false)
            }
            dependencies.url(
                "RedProtect",
                "https://www.spigotmc.org/resources/redprotect-anti-grief-server-protection-region-management-1-7-1-19.15841/"
            ) {
                required.set(false)
            }
            dependencies.url(
                "GriefDefender",
                "https://www.spigotmc.org/resources/1-12-2-1-19-4-griefdefender-claim-plugin-grief-prevention-protection.68900/"
            ) {
                required.set(false)
            }
            dependencies.url("WorldGuard", "https://dev.bukkit.org/projects/worldguard") {
                required.set(false)
            }
            dependencies.url("minePlots", "https://builtbybit.com/resources/mineplots.21646/") {
                required.set(false)
            }
            dependencies.url(
                "LocketteX",
                "https://www.spigotmc.org/resources/lockettex-optimized-simple-chest-protection-plugin.73184/"
            ) {
                required.set(false)
            }
            dependencies.url("LWC", "https://www.spigotmc.org/resources/lwc-extended.69551/") {
                required.set(false)
            }
        }
    }
}