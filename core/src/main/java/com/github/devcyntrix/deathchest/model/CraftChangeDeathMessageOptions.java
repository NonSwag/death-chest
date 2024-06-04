package com.github.devcyntrix.deathchest.model;

import com.github.devcyntrix.deathchest.api.model.ChangeDeathMessageOptions;
import com.google.gson.annotations.SerializedName;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CraftChangeDeathMessageOptions(
        @SerializedName("enabled") boolean enabled,
        @SerializedName("message") String[] message) implements ChangeDeathMessageOptions {

    @Contract("null -> new")
    public static @NotNull CraftChangeDeathMessageOptions load(@Nullable ConfigurationSection section) {
        if (section == null)
            section = new MemoryConfiguration();

        boolean enabled = section.getBoolean("enabled", false);
        String message = section.getString("message");
        String[] coloredMessage = null;
        if (message != null)
            coloredMessage = ChatColor.translateAlternateColorCodes('&', message).split("\n");

        return new CraftChangeDeathMessageOptions(enabled, coloredMessage);
    }

}
