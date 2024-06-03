package com.github.devcyntrix.deathchest.api.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DeathChestConfig {
    @NotNull
    BreakAnimationOptions breakAnimationOptions();

    @NotNull
    ChangeDeathMessageOptions changeDeathMessageOptions();

    @NotNull
    ChestOptions chestOptions();

    @NotNull
    GlobalNotificationOptions globalNotificationOptions();

    @NotNull
    HologramOptions hologramOptions();

    @NotNull
    InventoryOptions inventoryOptions();

    @NotNull
    ParticleOptions particleOptions();

    @NotNull
    PlayerNotificationOptions playerNotificationOptions();

    @NotNull
    String durationFormat();

    @NotNull
    WorldAliasConfig worldAlias();

    @NotNull
    WorldFilterConfig worldChestProtectionFilter();

    @NotNull
    WorldFilterConfig worldFilterConfig();

    @Nullable
    String preferredBlockBreakAnimationService();

    boolean autoUpdate();

    boolean debug();

    boolean updateChecker();

    int configVersion();
}
