package com.github.devcyntrix.deathchest.api.controller;

import com.github.devcyntrix.deathchest.api.hologram.Hologram;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;

public interface HologramController extends Closeable {
    @NotNull
    Hologram spawnHologram(@NotNull Location location, double lineHeight);
}
