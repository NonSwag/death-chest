import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = project(":api").group
version = project(":api").version

repositories {
    mavenCentral()
    maven("https://repository.minecodes.pl/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":core"))

    compileOnly("com.google.inject:guice:7.0.0")
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-platform-bukkit:4.3.3")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    // test dependencies

    testImplementation(project(":api"))
    testImplementation(project(":core"))

    testCompileOnly("org.jetbrains:annotations:24.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0-M2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.89.0")

    testImplementation("me.clip:placeholderapi:2.11.2") { isTransitive = false }
    testImplementation("org.slf4j:slf4j-simple:2.1.0-alpha1")

    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showCauses = true
        showExceptions = true
    }
}

bukkit {
    name = "DeathChest"
    main = "com.github.devcyntrix.deathchest.DeathChestSpigotPlugin"
    author = "CyntrixAlgorithm"
    contributors = listOf("NonSwag")
    apiVersion = "1.17"
    website = "https://www.spigotmc.org/resources/death-chest.101066/"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    softDepend = listOf(
        "WorldGuard",
        "ProtocolLib",
        "GriefPrevention",
        "GriefDefender",
        "PlaceholderAPI",
        "RedProtect",
        "minePlots",
        "LocketteX",
        "LWC"
    )
    libraries = listOf(
        "org.apache.commons:commons-text:1.10.0",
        "com.google.inject:guice:7.0.0",
        "cloud.commandframework:cloud-core:1.7.1",
        "cloud.commandframework:cloud-bukkit:1.7.1",
        "net.kyori:adventure-platform-bukkit:4.3.0",
        "net.kyori:adventure-text-minimessage:4.14.0",
        "net.kyori:adventure-text-serializer-legacy:4.14.0",
        "org.bstats:bstats-bukkit:3.0.2"
    )
    permissions {
        register("deathchest.command.report") {
            description = "The permission to create, read and delete reports of the plugin"
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("deathchest.command.deleteInWorld") {
            description = "The permission to delete all chests in all or a specific world"
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("deathchest.command.reload") {
            description = "The permission to reload the configuration file of the DeathChest plugin"
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("deathchest.update") {
            description = "Notifies the player about plugin updates."
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("deathchest.admin") {
            default = BukkitPluginDescription.Permission.Default.OP
            children = listOf(
                "deathchest.command.deathchest",
                "deathchest.command.report",
                "deathchest.command.deleteInWorld",
                "deathchest.command.reload"
            )
        }
    }
    commands {
        register("deathchest") {
            description = "The admin command for reloading the plugin's configuration"
            permission = "deathchest.command.deathchest"
        }
    }
}