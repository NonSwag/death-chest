package com.github.devcyntrix.deathchest.report;

import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;

public record CraftPluginInfo(@SerializedName("name") String name,
                              @SerializedName("version") String version,
                              @SerializedName("enabled") boolean enabled,
                              @SerializedName("authors") List<String> authors,
                              @SerializedName("dependencies") List<String> dependencies) {

    public static CraftPluginInfo of(Plugin plugin) {
        PluginDescriptionFile description = plugin.getDescription();
        return new CraftPluginInfo(description.getName(), description.getVersion(), Bukkit.getPluginManager().isPluginEnabled(plugin), description.getAuthors(), description.getDepend());
    }

}
