package com.github.devcyntrix.deathchest.hologram;

import com.github.devcyntrix.deathchest.api.hologram.HologramTextLine;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NativeHologramTextLine implements HologramTextLine {

    private final UUID armorStand;

    @NotNull
    private Location location;

    public NativeHologramTextLine(@NotNull Location location, @NotNull String text) {
        Preconditions.checkNotNull(location.getWorld());
        this.location = location;
        ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);
        stand.setMarker(true);
        stand.setInvisible(true);
        stand.setCustomName(text);
        stand.setCustomNameVisible(true);
        stand.setPersistent(false);
        this.armorStand = stand.getUniqueId();
    }

    public void teleport(@NotNull Location location) {
        this.location = location;
        ArmorStand stand = getArmorStand();
        if (stand == null || !stand.isValid()) return;
        stand.teleport(location);
    }

    @Override
    public void rename(@NotNull String text) {
        ArmorStand stand = getArmorStand();
        if (stand == null || !stand.isValid()) return;
        stand.setCustomName(text);
    }

    @Override
    public void remove() {
        var entity = getArmorStand();
        if (entity == null || !entity.isValid()) return;
        entity.remove();
    }


    public @NotNull Location getLocation() {
        return location.clone();
    }

    public @Nullable ArmorStand getArmorStand() {
        return (ArmorStand) Bukkit.getEntity(armorStand);
    }
}
