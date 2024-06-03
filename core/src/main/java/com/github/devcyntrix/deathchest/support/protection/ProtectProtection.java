package com.github.devcyntrix.deathchest.support.protection;

import com.github.devcyntrix.deathchest.api.protection.ProtectionService;
import net.thenextlvl.protect.area.AreaProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * <a href="https://hangar.papermc.io/TheNextLvl/Protect">Protect</a>
 */
public class ProtectProtection implements ProtectionService {
    private final net.thenextlvl.protect.service.ProtectionService protectionService = Objects.requireNonNull(
            Bukkit.getServicesManager().load(net.thenextlvl.protect.service.ProtectionService.class)
    );
    private final AreaProvider areaProvider = Objects.requireNonNull(
            Bukkit.getServicesManager().load(AreaProvider.class)
    );

    @Override
    public boolean canBuild(@NotNull Player player, @NotNull Location location, @NotNull Material material) {
        return areaProvider.getArea(location).getFlag(ProtectDeathChestFlag.FLAG) && protectionService.canBuild(player, location);
    }
}
