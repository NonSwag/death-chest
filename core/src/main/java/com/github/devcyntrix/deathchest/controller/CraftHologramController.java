package com.github.devcyntrix.deathchest.controller;

import com.github.devcyntrix.deathchest.api.DeathChestService;
import com.github.devcyntrix.deathchest.api.controller.HologramController;
import com.github.devcyntrix.deathchest.api.hologram.Hologram;
import com.github.devcyntrix.deathchest.hologram.NativeHologram;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@Singleton
@RequiredArgsConstructor
public class CraftHologramController implements HologramController {
    private final DeathChestService service;
    private final Set<Hologram> holograms = new HashSet<>();

    @Override
    public @NotNull Hologram spawnHologram(@NotNull Location location, double lineHeight) {
        service.debug(0, "Creating new hologram at " + formatLocation(location) + "...");
        var hologram = new NativeHologram(this, location, lineHeight);
        this.holograms.add(hologram);
        return hologram;
    }

    private String formatLocation(Location location) {
        return String.format("%d, %d, %d in world %s", location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName());
    }

    @Override
    public void close() {
        service.debug(0, "Deleting all holograms...");
        this.holograms.forEach(Hologram::delete);
        this.holograms.clear();
    }
}
