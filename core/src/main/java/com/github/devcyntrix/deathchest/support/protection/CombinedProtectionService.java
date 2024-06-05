package com.github.devcyntrix.deathchest.support.protection;

import com.github.devcyntrix.deathchest.api.protection.ProtectionService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@RequiredArgsConstructor
public class CombinedProtectionService implements ProtectionService {
    private final ProtectionService[] services;

    @Override
    public boolean canBuild(@NotNull Player player, @NotNull Location location, @NotNull Material material) {
        return Arrays.stream(services)
                .map(s -> s.canBuild(player, location, material))
                .reduce(Boolean::equals)
                .orElse(true); // Default is true
    }
}
