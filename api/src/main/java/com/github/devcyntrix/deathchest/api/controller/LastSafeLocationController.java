package com.github.devcyntrix.deathchest.api.controller;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface LastSafeLocationController {
    void updatePosition(Player player);

    @Nullable
    Location getPosition(Player player);
}
