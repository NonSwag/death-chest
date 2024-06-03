package com.github.devcyntrix.deathchest.api.controller;

import com.github.devcyntrix.deathchest.api.ChestView;
import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.audit.AuditManager;
import com.github.devcyntrix.deathchest.api.model.DeathChestConfig;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.api.storage.DeathChestStorage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.util.Collection;
import java.util.logging.Logger;

public interface DeathChestController extends Closeable {
    DeathChestService getService();

    Logger getLogger();

    AuditManager getAuditManager();

    DeathChestStorage getStorage();

    void registerAdapter(ChestView adapter);

    void loadChests();

    void loadChests(World world);

    DeathChestModel createChest(@NotNull Location location, long expireAt, @Nullable Player player, ItemStack @NotNull ... items);

    DeathChestModel createChest(@NotNull Location location, long createdAt, long expireAt, @Nullable Player player, boolean isProtected, ItemStack @NotNull ... items);

    @Nullable
    DeathChestModel getChest(@NotNull Location location);

    @NotNull
    Collection<DeathChestModel> getChests();

    @NotNull
    Collection<DeathChestModel> getChests(World world);

    boolean isAccessibleBy(@NotNull DeathChestModel model, @NotNull Player player);

    void destroyChest(DeathChestModel model);

    void unloadChests(World world, boolean save);

    void saveChests();

    void saveChests(@NotNull World world);

    DeathChestConfig getConfig();
}
