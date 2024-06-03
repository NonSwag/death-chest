package com.github.devcyntrix.deathchest.config;

import com.github.devcyntrix.deathchest.api.model.WorldAliasConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record CraftWorldAliasConfig(Map<String, Object> aliases) implements WorldAliasConfig {

    @Contract("null -> new")
    public static @NotNull CraftWorldAliasConfig load(ConfigurationSection section) {
        if (section == null)
            return new CraftWorldAliasConfig(Map.of());

        Map<String, Object> aliases = section.getValues(false);
        if (aliases.isEmpty())
            return new CraftWorldAliasConfig(Map.of());

        return new CraftWorldAliasConfig(Map.copyOf(aliases));
    }

    public @NotNull String getAlias(@NotNull String worldName) {
        return (String) aliases.getOrDefault(worldName, worldName);
    }
}
