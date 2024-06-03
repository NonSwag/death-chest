package com.github.devcyntrix.deathchest.config;

import com.github.devcyntrix.deathchest.api.model.*;
import com.google.gson.annotations.SerializedName;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CraftDeathChestConfig(
        @SerializedName("config-version") int configVersion,
        @SerializedName("debug") boolean debug,
        @SerializedName("update-checker") boolean updateChecker,
        @SerializedName("auto-update") boolean autoUpdate,
        @SerializedName("duration-format") @NotNull String durationFormat,
        @SerializedName("chest") @NotNull ChestOptions chestOptions,
        @SerializedName("inventory") @NotNull InventoryOptions inventoryOptions,
        @SerializedName("hologram") @NotNull HologramOptions hologramOptions,
        @SerializedName("particle") @NotNull ParticleOptions particleOptions,
        @SerializedName("break-effect") @NotNull BreakAnimationOptions breakAnimationOptions,
        @SerializedName("player-notification") @NotNull PlayerNotificationOptions playerNotificationOptions,
        @SerializedName("global-notification") @NotNull GlobalNotificationOptions globalNotificationOptions,
        @SerializedName("change-death-message") @NotNull ChangeDeathMessageOptions changeDeathMessageOptions,
        @SerializedName("world-filter") @NotNull WorldFilterConfig worldFilterConfig,
        @SerializedName("world-chest-protection-filter") @NotNull WorldFilterConfig worldChestProtectionFilter,
        @SerializedName("world-alias") @NotNull WorldAliasConfig worldAlias,
        @SerializedName("preferred-animation-service") @Nullable String preferredBlockBreakAnimationService
) implements DeathChestConfig {

    public static final int CONFIG_VERSION = 3;

    public static CraftDeathChestConfig load(FileConfiguration config) {
        int configVersion = config.getInt("config-version");
        boolean debug = config.getBoolean("debug", false);
        boolean updateCheck = config.getBoolean("update-checker", true);
        boolean autoUpdate = config.getBoolean("auto-update", true);
        String durationFormat = config.getString("duration-format", "mm:ss");

        CraftChestOptions chestOptions = CraftChestOptions.load(config.getConfigurationSection("chest"));

        // Inventory
        CraftInventoryOptions inventoryOptions = CraftInventoryOptions.load(config.getConfigurationSection("inventory"));

        // Hologram
        CraftHologramOptions hologramOptions = CraftHologramOptions.load(config.getConfigurationSection("hologram"));

        // Particle
        CraftParticleOptions particleOptions = CraftParticleOptions.load(config.getConfigurationSection("particle"));

        // Effect
        CraftBreakAnimationOptions breakAnimationOptions = CraftBreakAnimationOptions.load(config.getConfigurationSection("break-effect"));

        // Notification
        CraftPlayerNotificationOptions playerNotificationOptions = CraftPlayerNotificationOptions.load(config.getConfigurationSection("player-notification"));
        CraftGlobalNotificationOptions globalNotificationOptions = CraftGlobalNotificationOptions.load(config.getConfigurationSection("global-notification"));

        CraftChangeDeathMessageOptions changeDeathMessageOptions = CraftChangeDeathMessageOptions.load(config.getConfigurationSection("change-death-message"));

        CraftWorldFilterConfig worldFilterConfig = CraftWorldFilterConfig.load(config.getConfigurationSection("world-filter"));
        CraftWorldFilterConfig worldChestProtectionFilterConfig = CraftWorldFilterConfig.load(config.getConfigurationSection("world-chest-protection-filter"));
        CraftWorldAliasConfig worldAliasConfig = CraftWorldAliasConfig.load(config.getConfigurationSection("world-alias"));

        String preferredAnimationService = config.getString("preferred-animation-service");

        return new CraftDeathChestConfig(configVersion, debug, updateCheck, autoUpdate, durationFormat, chestOptions, inventoryOptions, hologramOptions, particleOptions, breakAnimationOptions, playerNotificationOptions, globalNotificationOptions, changeDeathMessageOptions, worldFilterConfig, worldChestProtectionFilterConfig, worldAliasConfig, preferredAnimationService);
    }

}
