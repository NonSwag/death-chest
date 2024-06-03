package com.github.devcyntrix.deathchest.api.hologram;

import com.github.devcyntrix.deathchest.api.controller.HologramController;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface Hologram {
    @NotNull
    HologramController getService();

    @NotNull Location getLocation();

    void teleport(@NotNull Location location);

    HologramTextLine appendLine(@NotNull String text);

    void delete();
}
