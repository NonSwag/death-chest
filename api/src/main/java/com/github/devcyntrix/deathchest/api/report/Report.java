package com.github.devcyntrix.deathchest.api.report;

import com.github.devcyntrix.deathchest.DeathChestPlugin;
import com.github.devcyntrix.deathchest.config.DeathChestConfig;
import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public interface Report {
    Date date();

    Set<PluginInfo> plugins();

    DeathChestConfig config();

    Map<String, Object> extra();
}
