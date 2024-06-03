package com.github.devcyntrix.deathchest.api.hologram;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface HologramTextLine {
    Location getLocation();

    void rename(@NotNull String text);

    void remove();

    void teleport(@NotNull Location location);
}
