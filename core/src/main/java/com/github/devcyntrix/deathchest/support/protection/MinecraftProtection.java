package com.github.devcyntrix.deathchest.support.protection;

import com.github.devcyntrix.deathchest.api.protection.ProtectionService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MinecraftProtection implements ProtectionService {

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean canBuild(@NotNull Player player, @NotNull Location location, @NotNull Material material) {
        int spawnRadius = Bukkit.getSpawnRadius();
        if (spawnRadius <= 0) return true;

        var min = location.getWorld().getSpawnLocation().clone().subtract(spawnRadius, 0, spawnRadius);
        var max = location.getWorld().getSpawnLocation().clone().add(spawnRadius, 0, spawnRadius);

        return !(location.getBlockX() >= min.getBlockX() && location.getBlockX() <= max.getBlockX() &&
                 location.getBlockZ() >= min.getBlockZ() && location.getBlockZ() <= max.getBlockZ());
    }
}
