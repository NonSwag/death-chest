package com.github.devcyntrix.deathchest.config;

import com.github.devcyntrix.deathchest.api.model.NoExpirationPermission;
import com.google.gson.annotations.SerializedName;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CraftNoExpirationPermission(
        @SerializedName("enabled") boolean enabled,
        @SerializedName("permission") String permission) implements NoExpirationPermission {

    public static final String DEFAULT_PERMISSION = "deathchest.stays-forever";

    @Contract("null -> new")
    public static @NotNull CraftNoExpirationPermission load(@Nullable ConfigurationSection section) {
        if (section == null)
            section = new MemoryConfiguration();

        boolean enabled = section.getBoolean("enabled", false);
        String permission = section.getString("permission", DEFAULT_PERMISSION);
        return new CraftNoExpirationPermission(enabled, permission);
    }
}
