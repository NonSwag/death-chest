import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

group = project(":api").group
version = project(":api").version

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":core"))

    compileOnly("com.google.inject:guice:7.0.0")
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

paper {
    name = "DeathChest"
    main = "com.github.devcyntrix.deathchest.DeathChestPaperPlugin"
    author = "CyntrixAlgorithm"
    contributors = listOf("NonSwag")
    apiVersion = "1.20"
    website = "https://www.spigotmc.org/resources/death-chest.101066/"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    serverDependencies {
        register("WorldGuard") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
        register("ProtocolLib") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
        register("GriefPrevention") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
        register("GriefDefender") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
        register("PlaceholderAPI") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
        register("RedProtect") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
        register("minePlots") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
        register("LocketteX") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
        register("LWC") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
    }
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
}