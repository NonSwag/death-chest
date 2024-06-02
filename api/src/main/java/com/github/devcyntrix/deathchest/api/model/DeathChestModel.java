package com.github.devcyntrix.deathchest.api.model;

import com.github.devcyntrix.deathchest.api.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.util.Set;

public interface DeathChestModel extends InventoryHolder {
    @NotNull
    BlockState getPrevious();

    @Nullable
    Hologram getHologram();

    @Nullable
    Integer getBreakingEntityId();

    @NotNull
    Location getLocation();

    @Nullable
    OfflinePlayer getOwner();

    @NotNull
    Set<Closeable> getTasks();

    @Nullable
    World getWorld();

    boolean isDeleting();

    boolean isProtected();

    long getCreatedAt();

    long getExpireAt();

    void setBreakingEntityId(@Nullable Integer breakingEntityId);

    void setCreatedAt(long createdAt);

    void setDeleting(boolean deleting);

    void setExpireAt(long expireAt);

    void setHologram(@Nullable Hologram hologram);

    void setInventory(@NotNull Inventory inventory);

    void setLocation(@NotNull Location location);

    void setOwner(@Nullable OfflinePlayer owner);

    void setPrevious(@NotNull BlockState blockState);

    void setProtected(boolean isProtected);

    void dropItems();

    void dropItems(@NotNull Location location);

    void cancelTasks();
}
