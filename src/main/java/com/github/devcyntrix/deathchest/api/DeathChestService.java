package com.github.devcyntrix.deathchest.api;

import com.github.devcyntrix.deathchest.api.animation.AnimationService;
import com.github.devcyntrix.deathchest.api.hologram.HologramService;
import com.github.devcyntrix.deathchest.api.protection.ProtectionService;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.stream.Stream;

public interface DeathChestService {

    @Nullable DeathChest getLastChest(@NotNull Player player);

    boolean canPlaceChestAt(@NotNull Location location);

    @NotNull DeathChest createDeathChest(@NotNull Location location, ItemStack @NotNull ... items);

    @NotNull DeathChest createDeathChest(@NotNull Location location, @Nullable OfflinePlayer player, ItemStack @NotNull ... items);

    @NotNull DeathChest createDeathChest(@NotNull Location location, long expireAt, @Nullable OfflinePlayer player, ItemStack @NotNull ... items);

    @NotNull DeathChest createDeathChest(@NotNull DeathChestSnapshot snapshot);

    default @NotNull DeathChest createDeathChest(@NotNull Location location, long createdAt, long expireAt, @Nullable OfflinePlayer player, ItemStack @NotNull ... items) {
        return createDeathChest(location, createdAt, expireAt, player, false, items);
    }

    @NotNull DeathChest createDeathChest(@NotNull Location location, long createdAt, long expireAt, @Nullable OfflinePlayer player, boolean isProtected, ItemStack @NotNull ... items);

    @NotNull Stream<@NotNull DeathChest> getChests();

    void saveChests() throws IOException;

    default boolean hasHologram() {
        return getHologramService() != null;
    }

    @Nullable HologramService getHologramService();

    default boolean hasAnimation() {
        return getAnimationService() != null;
    }

    @Nullable AnimationService getAnimationService();

    @NotNull ProtectionService getProtectionService();
}
